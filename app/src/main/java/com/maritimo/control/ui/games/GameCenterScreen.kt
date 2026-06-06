package com.maritimo.control.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maritimo.control.data.remote.dto.AtraqueDto
import com.maritimo.control.data.remote.dto.InspeccionDto
import com.maritimo.control.ui.theme.*
import com.maritimo.control.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCenterScreen(
    onNavigateToGame: (String) -> Unit,
    viewModel: EmergencySimulationViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    authViewModel: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isAdmin by authViewModel.isAdminRole.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    // Dialog flags
    var showInspeccionDialog by remember { mutableStateOf(false) }
    var selectedInspeccionForEdit by remember { mutableStateOf<InspeccionDto?>(null) }
    var showAtraqueDialog by remember { mutableStateOf(false) }
    var selectedAtraqueForEdit by remember { mutableStateOf<AtraqueDto?>(null) }

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
                    Text(
                        text = "Operaciones de Seguridad",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.loadData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar",
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
                        if (selectedTab == 0) {
                            selectedInspeccionForEdit = null
                            showInspeccionDialog = true
                        } else {
                            selectedAtraqueForEdit = null
                            showAtraqueDialog = true
                        }
                    },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceColor,
                contentColor = PrimaryBlue
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Inspecciones", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Atraques", fontWeight = FontWeight.Bold) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = if (selectedTab == 0) state.inspeccionSearch else state.atraqueSearch,
                    onValueChange = {
                        if (selectedTab == 0) viewModel.updateInspeccionSearch(it)
                        else viewModel.updateAtraqueSearch(it)
                    },
                    placeholder = {
                        Text(if (selectedTab == 0) "Filtrar por resultado..." else "Filtrar por estado del atraque...")
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // main list container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading && state.inspecciones.isEmpty() && state.atraques.isEmpty()) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    } else {
                        if (selectedTab == 0) {
                            // Inspecciones Tab
                            if (state.inspecciones.isEmpty()) {
                                Text("No hay inspecciones.", color = TextSecondary)
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(bottom = 80.dp)
                                ) {
                                    items(state.inspecciones, key = { it.id }) { inspeccion ->
                                        InspeccionCardItem(
                                            inspeccion = inspeccion,
                                            isAdmin = isAdmin,
                                            onEdit = {
                                                selectedInspeccionForEdit = inspeccion
                                                showInspeccionDialog = true
                                            },
                                            onDelete = {
                                                viewModel.deleteInspeccion(inspeccion.id)
                                            }
                                        )
                                    }

                                    if (state.hasMoreInspecciones) {
                                        item {
                                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                Button(
                                                    onClick = { viewModel.loadMoreInspecciones() },
                                                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                                                ) {
                                                    Text("Cargar más")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Atraques Tab
                            if (state.atraques.isEmpty()) {
                                Text("No hay atraques.", color = TextSecondary)
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(bottom = 80.dp)
                                ) {
                                    items(state.atraques, key = { it.id }) { atraque ->
                                        AtraqueCardItem(
                                            atraque = atraque,
                                            isAdmin = isAdmin,
                                            onEdit = {
                                                selectedAtraqueForEdit = atraque
                                                showAtraqueDialog = true
                                            },
                                            onDelete = {
                                                viewModel.deleteAtraque(atraque.id)
                                            }
                                        )
                                    }

                                    if (state.hasMoreAtraques) {
                                        item {
                                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                Button(
                                                    onClick = { viewModel.loadMoreAtraques() },
                                                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                                                ) {
                                                    Text("Cargar más")
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
        }
    }

    // Dialogo Inspeccion
    if (showInspeccionDialog) {
        InspeccionFormDialog(
            inspeccion = selectedInspeccionForEdit,
            onDismiss = { showInspeccionDialog = false },
            onConfirm = { atraqueId, fecha, resultado, obs ->
                if (selectedInspeccionForEdit == null) {
                    viewModel.createInspeccion(atraqueId, fecha, resultado, obs) { showInspeccionDialog = false }
                } else {
                    viewModel.updateInspeccion(selectedInspeccionForEdit!!.id, atraqueId, fecha, resultado, obs) { showInspeccionDialog = false }
                }
            }
        )
    }

    // Dialogo Atraque
    if (showAtraqueDialog) {
        AtraqueFormDialog(
            atraque = selectedAtraqueForEdit,
            onDismiss = { showAtraqueDialog = false },
            onConfirm = { buqueId, muelleId, capitanId, fechaIng, fechaSal, estado ->
                if (selectedAtraqueForEdit == null) {
                    viewModel.createAtraque(buqueId, muelleId, capitanId, fechaIng, fechaSal, estado) { showAtraqueDialog = false }
                } else {
                    viewModel.updateAtraque(selectedAtraqueForEdit!!.id, buqueId, muelleId, capitanId, fechaIng, fechaSal, estado) { showAtraqueDialog = false }
                }
            }
        )
    }
}

@Composable
fun InspeccionCardItem(
    inspeccion: InspeccionDto,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val resultLower = inspeccion.resultado.lowercase()
    val (statusLabel, statusColor, statusIcon) = when {
        resultLower.contains("aprobado") -> Triple("Aprobado", Success, Icons.Default.CheckCircle)
        resultLower.contains("pendiente") -> Triple("Pendiente", Warning, Icons.Default.HourglassEmpty)
        else -> Triple("Rechazado", ErrorColor, Icons.Default.Dangerous)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Border, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Inspección #${inspeccion.id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = statusIcon,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = statusLabel,
                                color = statusColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
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

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Divider, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "BUQUE", fontSize = 9.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
                    Text(
                        text = inspeccion.atraque.buque.nombre,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "MUELLE / ATRAQUE", fontSize = 9.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Muelle ${inspeccion.atraque.muelle.codigo} (Atraque #${inspeccion.atraque.id})",
                        fontSize = 13.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SoftBlue)
                    .padding(10.dp)
            ) {
                Text(
                    text = "OBSERVACIONES Y NOTAS",
                    fontSize = 9.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = inspeccion.observaciones.ifBlank { "Sin observaciones." },
                    fontSize = 12.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = formatInspectionDate(inspeccion.fechaInspeccion),
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AtraqueCardItem(
    atraque: AtraqueDto,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Border, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Atraque #${atraque.id} - Muelle ${atraque.muelle.codigo}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = AccentBlue.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = atraque.estado,
                            color = AccentBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
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

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Divider, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Buque: ${atraque.buque.nombre} (${atraque.buque.matricula})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Capitán: ${atraque.capitan.nombres} ${atraque.capitan.apellidos}",
                fontSize = 13.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ingreso: ${formatDateTime(atraque.fechaIngreso)} | Salida: ${formatDateTime(atraque.fechaSalida)}",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspeccionFormDialog(
    inspeccion: InspeccionDto?,
    onDismiss: () -> Unit,
    onConfirm: (Int, String, String, String) -> Unit
) {
    var atraqueIdText by remember { mutableStateOf(inspeccion?.atraque?.id?.toString() ?: "") }
    var fecha by remember { mutableStateOf(inspeccion?.fechaInspeccion ?: "2026-06-05T08:00:00Z") }
    var resultado by remember { mutableStateOf(inspeccion?.resultado ?: "Aprobado") }
    var observaciones by remember { mutableStateOf(inspeccion?.observaciones ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (inspeccion == null) "Nueva Inspección" else "Editar Inspección", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = atraqueIdText, onValueChange = { atraqueIdText = it }, label = { Text("ID de Atraque") })
                OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha y Hora") })
                OutlinedTextField(value = resultado, onValueChange = { resultado = it }, label = { Text("Resultado (Aprobado/Pendiente/Rechazado)") })
                OutlinedTextField(value = observaciones, onValueChange = { observaciones = it }, label = { Text("Observaciones") })
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(atraqueIdText.toIntOrNull() ?: 1, fecha, resultado, observaciones) },
                enabled = atraqueIdText.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtraqueFormDialog(
    atraque: AtraqueDto?,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int, String, String, String) -> Unit
) {
    var buqueIdText by remember { mutableStateOf(atraque?.buque?.id?.toString() ?: "") }
    var muelleIdText by remember { mutableStateOf(atraque?.muelle?.id?.toString() ?: "") }
    var capitanIdText by remember { mutableStateOf(atraque?.capitan?.id?.toString() ?: "") }
    var fechaIng by remember { mutableStateOf(atraque?.fechaIngreso ?: "2026-06-05T08:00:00Z") }
    var fechaSal by remember { mutableStateOf(atraque?.fechaSalida ?: "2026-06-06T18:00:00Z") }
    var estado by remember { mutableStateOf(atraque?.estado ?: "activo") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (atraque == null) "Nuevo Atraque" else "Editar Atraque", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = buqueIdText, onValueChange = { buqueIdText = it }, label = { Text("ID de Buque") })
                OutlinedTextField(value = muelleIdText, onValueChange = { muelleIdText = it }, label = { Text("ID de Muelle") })
                OutlinedTextField(value = capitanIdText, onValueChange = { capitanIdText = it }, label = { Text("ID de Capitán") })
                OutlinedTextField(value = fechaIng, onValueChange = { fechaIng = it }, label = { Text("Fecha Ingreso") })
                OutlinedTextField(value = fechaSal, onValueChange = { fechaSal = it }, label = { Text("Fecha Salida") })
                OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        buqueIdText.toIntOrNull() ?: 1,
                        muelleIdText.toIntOrNull() ?: 1,
                        capitanIdText.toIntOrNull() ?: 1,
                        fechaIng,
                        fechaSal,
                        estado
                    )
                },
                enabled = buqueIdText.isNotBlank() && muelleIdText.isNotBlank() && capitanIdText.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

fun formatInspectionDate(dateString: String): String {
    return try {
        val date = dateString.substringBefore("T")
        val time = dateString.substringAfter("T").substringBefore(":")
        val minutes = dateString.substringAfter(":").substringBefore(":")
        "Realizada el: $date a las $time:$minutes hs"
    } catch (e: Exception) {
        dateString
    }
}

fun formatDateTime(dateTimeString: String): String {
    return try {
        val datePart = dateTimeString.substringBefore("T")
        val timePart = dateTimeString.substringAfter("T").substringBefore(":")
        val minutes = dateTimeString.substringAfter(":").substringBefore(":")
        "$datePart $timePart:$minutes"
    } catch (e: Exception) {
        dateTimeString
    }
}