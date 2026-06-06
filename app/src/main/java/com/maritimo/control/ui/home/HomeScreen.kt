package com.maritimo.control.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maritimo.control.data.remote.dto.BuqueDto
import com.maritimo.control.ui.theme.*
import com.maritimo.control.ui.viewmodel.CartViewModel
import com.maritimo.control.ui.viewmodel.HomeViewModel
import com.maritimo.control.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onCourseClick: (Int) -> Unit,
    onCatalogClick: () -> Unit,
    onJoinClassClick: () -> Unit,
    onIATutorClick: () -> Unit,
    onOpenCart: () -> Unit,
    onMyClassesClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onCertificatesClick: () -> Unit,
    cartViewModel: CartViewModel,
    viewModel: HomeViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    authViewModel: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isAdmin by authViewModel.isAdminRole.collectAsState()

    var showCreateEditDialog by remember { mutableStateOf(false) }
    var selectedBuqueForEdit by remember { mutableStateOf<BuqueDto?>(null) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedBuqueForDetail by remember { mutableStateOf<BuqueDto?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBoat,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Marítimo Control",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadHomeData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refrescar",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar Sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                )
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = {
                        selectedBuqueForEdit = null
                        showCreateEditDialog = true
                    },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Buque")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Tarjeta de Bienvenida
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Border, RoundedCornerShape(16.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(PrimaryBlue, AccentBlue)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Panel de Control Marítimo",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Operador de turno: ${state.userName} (${if (isAdmin) "Admin/Staff" else "Lectura"})",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StatMiniBadge(
                                icon = Icons.Default.DirectionsBoat,
                                value = state.buques.size.toString(),
                                label = "Total Buques"
                            )
                            StatMiniBadge(
                                icon = Icons.Default.Anchor,
                                value = state.buques.filter { it.id % 2 == 0 }.size.toString(),
                                label = "En Puerto"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Buscar buque por nombre o matrícula...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Título de sección de listado
            Text(
                text = "Buques en Terminal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 2. Listado de Buques Real (LazyColumn)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading && state.buques.isEmpty()) {
                    CircularProgressIndicator(color = PrimaryBlue)
                } else if (state.buques.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Anchor,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No se encontraron buques en el sistema.",
                            color = TextSecondary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.buques, key = { it.id }) { buque ->
                            BuqueItemCard(
                                buque = buque,
                                isAdmin = isAdmin,
                                onClick = {
                                    selectedBuqueForDetail = buque
                                    showDetailDialog = true
                                },
                                onEdit = {
                                    selectedBuqueForEdit = buque
                                    showCreateEditDialog = true
                                },
                                onDelete = {
                                    viewModel.deleteBuque(buque.id)
                                }
                            )
                        }

                        if (state.hasMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (state.isLoading) {
                                        CircularProgressIndicator(color = PrimaryBlue, modifier = Modifier.size(24.dp))
                                    } else {
                                        Button(
                                            onClick = { viewModel.loadMoreBuques() },
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                                        ) {
                                            Text("Cargar más buques")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de Crear / Editar Buque
    if (showCreateEditDialog) {
        CreateEditBuqueDialog(
            buque = selectedBuqueForEdit,
            onDismiss = { showCreateEditDialog = false },
            onConfirm = { nombre, matricula, tipo, capCarga, pais ->
                val newBuque = BuqueDto(
                    id = selectedBuqueForEdit?.id ?: 0,
                    nombre = nombre,
                    matricula = matricula,
                    tipoBuque = tipo,
                    capacidadCarga = capCarga,
                    paisOrigen = pais
                )
                if (selectedBuqueForEdit == null) {
                    viewModel.createBuque(newBuque) { showCreateEditDialog = false }
                } else {
                    viewModel.updateBuque(newBuque.id, newBuque) { showCreateEditDialog = false }
                }
            }
        )
    }

    // Diálogo de Detalle
    if (showDetailDialog && selectedBuqueForDetail != null) {
        BuqueDetailDialog(
            buque = selectedBuqueForDetail!!,
            onDismiss = { showDetailDialog = false }
        )
    }
}

@Composable
fun StatMiniBadge(icon: ImageVector, value: String, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = value,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp,
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun BuqueItemCard(
    buque: BuqueDto,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val (statusLabel, statusColor) = when {
        buque.id % 3 == 0 -> "En Fondeo" to Warning
        buque.id % 2 == 0 -> "En Muelle" to Success
        else -> "En Tránsito" to Info
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buque.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            color = statusColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (isAdmin) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AccentBlue, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = ErrorColor, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Matrícula: ${buque.matricula}",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Divider, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "TIPO DE BUQUE", color = TextTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = buque.tipoBuque, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "BANDERA / ORIGEN", color = TextTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = buque.paisOrigen, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "CAP. CARGA", color = TextTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${buque.capacidadCarga} Ton", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditBuqueDialog(
    buque: BuqueDto?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(buque?.nombre ?: "") }
    var matricula by remember { mutableStateOf(buque?.matricula ?: "") }
    var tipo by remember { mutableStateOf(buque?.tipoBuque ?: "") }
    var capacidad by remember { mutableStateOf(buque?.capacidadCarga ?: "") }
    var pais by remember { mutableStateOf(buque?.paisOrigen ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (buque == null) "Nuevo Buque" else "Editar Buque", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = matricula, onValueChange = { matricula = it }, label = { Text("Matrícula") })
                OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo de Buque") })
                OutlinedTextField(value = capacidad, onValueChange = { capacidad = it }, label = { Text("Capacidad (Ton)") })
                OutlinedTextField(value = pais, onValueChange = { pais = it }, label = { Text("País de Origen") })
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombre, matricula, tipo, capacidad, pais) },
                enabled = nombre.isNotBlank() && matricula.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun BuqueDetailDialog(
    buque: BuqueDto,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = buque.nombre, fontWeight = FontWeight.Black, color = PrimaryBlue) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailField("Matrícula Oficial", buque.matricula)
                DetailField("Categoría de Buque", buque.tipoBuque)
                DetailField("Capacidad Nominal", "${buque.capacidadCarga} Toneladas métricas")
                DetailField("País de Abanderamiento", buque.paisOrigen)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun DetailField(label: String, value: String) {
    Column {
        Text(text = label.uppercase(), fontSize = 9.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 15.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}