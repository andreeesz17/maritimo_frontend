package com.maritimo.control.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maritimo.control.data.remote.dto.AtraqueDto
import com.maritimo.control.data.remote.dto.MuelleDto
import com.maritimo.control.data.remote.dto.PuertoDto
import com.maritimo.control.ui.theme.*
import com.maritimo.control.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyClassesScreen(
    onBack: () -> Unit,
    onClassDetail: (Int) -> Unit,
    onJoinClass: () -> Unit,
    viewModel: MuelleAllocationViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    authViewModel: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isAdmin by authViewModel.isAdminRole.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    // Dialog flags
    var showMuelleDialog by remember { mutableStateOf(false) }
    var selectedMuelleForEdit by remember { mutableStateOf<MuelleDto?>(null) }
    var showPuertoDialog by remember { mutableStateOf(false) }
    var selectedPuertoForEdit by remember { mutableStateOf<PuertoDto?>(null) }

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
                        text = "Gestión Terminal",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
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
                            selectedMuelleForEdit = null
                            showMuelleDialog = true
                        } else {
                            selectedPuertoForEdit = null
                            showPuertoDialog = true
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
                    text = { Text("Muelles", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Puertos", fontWeight = FontWeight.Bold) }
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
                    value = if (selectedTab == 0) state.muelleSearch else state.puertoSearch,
                    onValueChange = {
                        if (selectedTab == 0) viewModel.updateMuelleSearch(it)
                        else viewModel.updatePuertoSearch(it)
                    },
                    placeholder = {
                        Text(if (selectedTab == 0) "Buscar muelle por código..." else "Buscar puerto por nombre o ciudad...")
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
                    if (state.isLoading && state.muelles.isEmpty() && state.puertos.isEmpty()) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    } else {
                        if (selectedTab == 0) {
                            // Muelles Tab
                            if (state.muelles.isEmpty()) {
                                Text("No se encontraron muelles.", color = TextSecondary)
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(bottom = 80.dp)
                                ) {
                                    items(state.muelles, key = { it.id }) { muelle ->
                                        val muelleAtraque = state.atraques.find {
                                            it.muelle.id == muelle.id || it.muelle.codigo == muelle.codigo
                                        }
                                        MuelleCardItem(
                                            muelle = muelle,
                                            atraque = muelleAtraque,
                                            isAdmin = isAdmin,
                                            onEdit = {
                                                selectedMuelleForEdit = muelle
                                                showMuelleDialog = true
                                            },
                                            onDelete = {
                                                viewModel.deleteMuelle(muelle.id)
                                            }
                                        )
                                    }

                                    if (state.hasMoreMuelles) {
                                        item {
                                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                Button(
                                                    onClick = { viewModel.loadMoreMuelles() },
                                                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                                                ) {
                                                    Text("Cargar más muelles")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Puertos Tab
                            if (state.puertos.isEmpty()) {
                                Text("No se encontraron puertos.", color = TextSecondary)
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(bottom = 80.dp)
                                ) {
                                    items(state.puertos, key = { it.id }) { puerto ->
                                        PuertoCardItem(
                                            puerto = puerto,
                                            isAdmin = isAdmin,
                                            onEdit = {
                                                selectedPuertoForEdit = puerto
                                                showPuertoDialog = true
                                            },
                                            onDelete = {
                                                viewModel.deletePuerto(puerto.id)
                                            }
                                        )
                                    }

                                    if (state.hasMorePuertos) {
                                        item {
                                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                                Button(
                                                    onClick = { viewModel.loadMorePuertos() },
                                                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                                                ) {
                                                    Text("Cargar más puertos")
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

    // Dialogo Muelle
    if (showMuelleDialog) {
        MuelleFormDialog(
            muelle = selectedMuelleForEdit,
            puertos = state.puertos,
            onDismiss = { showMuelleDialog = false },
            onConfirm = { codigo, capacidad, estado, puertoId ->
                if (selectedMuelleForEdit == null) {
                    viewModel.createMuelle(codigo, capacidad, estado, puertoId) { showMuelleDialog = false }
                } else {
                    viewModel.updateMuelle(selectedMuelleForEdit!!.id, codigo, capacidad, estado, puertoId) { showMuelleDialog = false }
                }
            }
        )
    }

    // Dialogo Puerto
    if (showPuertoDialog) {
        PuertoFormDialog(
            puerto = selectedPuertoForEdit,
            onDismiss = { showPuertoDialog = false },
            onConfirm = { nombre, ciudad, capMax, estado ->
                val p = PuertoDto(
                    id = selectedPuertoForEdit?.id ?: 0,
                    nombre = nombre,
                    ciudad = ciudad,
                    capacidadMaximaBuques = capMax,
                    estado = estado
                )
                if (selectedPuertoForEdit == null) {
                    viewModel.createPuerto(p) { showPuertoDialog = false }
                } else {
                    viewModel.updatePuerto(p.id, p) { showPuertoDialog = false }
                }
            }
        )
    }
}

@Composable
fun MuelleCardItem(
    muelle: MuelleDto,
    atraque: AtraqueDto?,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val estadoLower = muelle.estado.lowercase()
    val (statusLabel, statusColor) = when {
        estadoLower == "disponible" -> "Disponible" to Success
        estadoLower == "ocupado" || atraque != null -> "Ocupado" to AccentBlue
        else -> "Mantenimiento" to ErrorColor
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
                        imageVector = Icons.Default.Anchor,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Muelle ${muelle.codigo}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            color = statusColor,
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

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Capacidad de atraque: ${muelle.capacidadAtraque} buques | Puerto: ${muelle.puerto.nombre}",
                fontSize = 13.sp,
                color = TextSecondary
            )

            if (atraque != null) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Divider, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "BUQUE ASIGNADO",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBoat,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = atraque.buque.nombre,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Matrícula: ${atraque.buque.matricula} | Capitán: ${atraque.capitan.nombres} ${atraque.capitan.apellidos}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SoftBlue)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoBerthRow(
                        icon = Icons.Default.Login,
                        label = "Ingreso",
                        value = formatDateTime(atraque.fechaIngreso)
                    )
                    InfoBerthRow(
                        icon = Icons.Default.Logout,
                        label = "Salida Est.",
                        value = formatDateTime(atraque.fechaSalida)
                    )
                }
            }
        }
    }
}

@Composable
fun PuertoCardItem(
    puerto: PuertoDto,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val (statusLabel, statusColor) = if (puerto.estado.lowercase() == "activo") {
        "Activo" to Success
    } else {
        "Inactivo" to ErrorColor
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
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = puerto.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            color = statusColor,
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

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ciudad: ${puerto.ciudad} | Capacidad Máxima: ${puerto.capacidadMaximaBuques} buques",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuelleFormDialog(
    muelle: MuelleDto?,
    puertos: List<PuertoDto>,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, Int) -> Unit
) {
    var codigo by remember { mutableStateOf(muelle?.codigo ?: "") }
    var capacidadText by remember { mutableStateOf(muelle?.capacidadAtraque?.toString() ?: "") }
    var estado by remember { mutableStateOf(muelle?.estado ?: "disponible") }
    var puertoIdSelected by remember { mutableIntStateOf(muelle?.puerto?.id ?: puertos.firstOrNull()?.id ?: 0) }

    var dropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (muelle == null) "Nuevo Muelle" else "Editar Muelle", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código") })
                OutlinedTextField(value = capacidadText, onValueChange = { capacidadText = it }, label = { Text("Capacidad") })
                OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado (disponible/ocupado/mantenimiento)") })

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = { dropdownExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                        val pName = puertos.find { it.id == puertoIdSelected }?.nombre ?: "Seleccionar Puerto"
                        Text("Puerto: $pName")
                    }
                    DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                        puertos.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.nombre) },
                                onClick = {
                                    puertoIdSelected = p.id
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(codigo, capacidadText.toIntOrNull() ?: 1, estado, puertoIdSelected) },
                enabled = codigo.isNotBlank() && puertoIdSelected != 0
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
fun PuertoFormDialog(
    puerto: PuertoDto?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, String) -> Unit
) {
    var nombre by remember { mutableStateOf(puerto?.nombre ?: "") }
    var ciudad by remember { mutableStateOf(puerto?.ciudad ?: "") }
    var capMaxText by remember { mutableStateOf(puerto?.capacidadMaximaBuques?.toString() ?: "") }
    var estado by remember { mutableStateOf(puerto?.estado ?: "activo") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (puerto == null) "Nuevo Puerto" else "Editar Puerto", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") })
                OutlinedTextField(value = capMaxText, onValueChange = { capMaxText = it }, label = { Text("Capacidad Máxima") })
                OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado (activo/inactivo)") })
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombre, ciudad, capMaxText.toIntOrNull() ?: 5, estado) },
                enabled = nombre.isNotBlank() && ciudad.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun InfoBerthRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentBlue,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(text = label, fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
        }
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