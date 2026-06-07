package com.maritimo.control.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.alpha
import com.maritimo.control.R
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
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var buqueIdToDelete by remember { mutableStateOf<Int?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        containerColor = AzulAbisal,
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
                    containerColor = AzulAbisal
                )
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(AzulAcero, CianElectrico)))
                        .clickable {
                            selectedBuqueForEdit = null
                            showCreateEditDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Buque", tint = Color.White)
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.5.dp, CianElectrico.copy(alpha = 0.25f)), RoundedCornerShape(24.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(AzulAbisal, Color(0xFF13254A))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "EcuPuerto Dashboard",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Operador: ${state.userName}",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (isAdmin) "Rol: Administrador" else "Rol: Operador de Consulta",
                                color = CianElectrico,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
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
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo_ecupuerto),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Buscar buque por nombre o matrícula...", color = Color.White.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CianElectrico) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = CianElectrico,
                    focusedLabelColor = CianElectrico,
                    cursorColor = CianElectrico,
                    unfocusedBorderColor = CianElectrico.copy(alpha = 0.15f),
                    focusedContainerColor = Color(0xFF1B2A4A).copy(alpha = 0.4f),
                    unfocusedContainerColor = Color(0xFF1B2A4A).copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Título de sección de listado
            Text(
                text = "Buques en Terminal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
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
                    CircularProgressIndicator(color = CianElectrico)
                } else if (state.buques.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Anchor,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No se encontraron buques en el sistema.",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 88.dp)
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
                                    buqueIdToDelete = buque.id
                                    showDeleteConfirmDialog = true
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
                                        CircularProgressIndicator(color = CianElectrico, modifier = Modifier.size(24.dp))
                                    } else {
                                        Button(
                                            onClick = { viewModel.loadMoreBuques() },
                                            colors = ButtonDefaults.buttonColors(containerColor = AzulAcero)
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

    if (showDeleteConfirmDialog && buqueIdToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text(
                    text = "Eliminar Buque",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar este buque? Esta acción no se puede deshacer.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        buqueIdToDelete?.let { viewModel.deleteBuque(it) }
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RojoCoral)
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancelar", color = Color.White.copy(alpha = 0.6f))
                }
            },
            containerColor = Color(0xFF0C162A),
            shape = RoundedCornerShape(24.dp)
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
            tint = CianElectrico,
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
        buque.id % 3 == 0 -> "En Fondeo" to AmbarAlerta
        buque.id % 2 == 0 -> "En Muelle" to VerdeEsmeralda
        else -> "En Tránsito" to AzulAcero
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = BlancoHielo),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(statusColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
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
                            color = statusColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = statusLabel,
                                color = statusColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }

                        if (isAdmin) {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AzulAcero, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = RojoCoral, modifier = Modifier.size(16.dp))
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
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "TIPO", color = TextTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = buque.tipoBuque, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "BANDERA", color = TextTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = buque.paisOrigen, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "CAPACIDAD", color = TextTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${buque.capacidadCarga} Ton", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
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
                DetailField("ID de Buque en Sistema", buque.id.toString())
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