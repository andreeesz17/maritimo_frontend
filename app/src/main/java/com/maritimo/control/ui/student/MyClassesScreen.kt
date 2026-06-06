package com.maritimo.control.ui.student

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextOverflow
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
    var showDetailMuelleDialog by remember { mutableStateOf(false) }
    var selectedMuelleForDetail by remember { mutableStateOf<MuelleDto?>(null) }
    var showDetailPuertoDialog by remember { mutableStateOf(false) }
    var selectedPuertoForDetail by remember { mutableStateOf<PuertoDto?>(null) }

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
                    containerColor = AzulAbisal
                )
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = {
                        viewModel.clearError()
                        if (selectedTab == 0) {
                            selectedMuelleForEdit = null
                            showMuelleDialog = true
                        } else {
                            selectedPuertoForEdit = null
                            showPuertoDialog = true
                        }
                    },
                    containerColor = AzulAcero,
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
            // Segmented Tab Selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1B2A4A).copy(alpha = 0.4f))
                    .border(BorderStroke(1.dp, GlassBorder), RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == 0) CianElectrico else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Muelles",
                            color = if (selectedTab == 0) Color.White else Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == 1) CianElectrico else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Puertos",
                            color = if (selectedTab == 1) Color.White else Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
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
                        Text(if (selectedTab == 0) "Buscar muelle por código..." else "Buscar puerto por nombre o ciudad...", color = Color.White.copy(alpha = 0.5f))
                    },
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

                Spacer(modifier = Modifier.height(16.dp))

                // main list container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading && state.muelles.isEmpty() && state.puertos.isEmpty()) {
                        CircularProgressIndicator(color = CianElectrico)
                    } else {
                        if (selectedTab == 0) {
                            // Muelles Tab
                            if (state.muelles.isEmpty()) {
                                Text("No se encontraron muelles.", color = Color.White.copy(alpha = 0.7f))
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
                                            onClick = {
                                                selectedMuelleForDetail = muelle
                                                showDetailMuelleDialog = true
                                            },
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
                                                    colors = ButtonDefaults.buttonColors(containerColor = AzulAcero)
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
                                Text("No se encontraron puertos.", color = Color.White.copy(alpha = 0.7f))
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
                                            onClick = {
                                                selectedPuertoForDetail = puerto
                                                showDetailPuertoDialog = true
                                            },
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
                                                    colors = ButtonDefaults.buttonColors(containerColor = AzulAcero)
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
            errorMessage = state.error,
            onDismiss = { showMuelleDialog = false; viewModel.clearError() },
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
            errorMessage = state.error,
            onDismiss = { showPuertoDialog = false; viewModel.clearError() },
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

    // Dialogos de Detalle Muelle y Puerto
    if (showDetailMuelleDialog && selectedMuelleForDetail != null) {
        AlertDialog(
            onDismissRequest = { showDetailMuelleDialog = false },
            title = { Text(text = "Muelle ${selectedMuelleForDetail!!.codigo}", fontWeight = FontWeight.Black, color = PrimaryBlue) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailFieldMuelle("ID de Muelle en Sistema", selectedMuelleForDetail!!.id.toString())
                    DetailFieldMuelle("Capacidad de Atraque", "${selectedMuelleForDetail!!.capacidadAtraque} buques")
                    DetailFieldMuelle("Estado del Muelle", selectedMuelleForDetail!!.estado.uppercase())
                    DetailFieldMuelle("Puerto Perteneciente", selectedMuelleForDetail!!.puerto.nombre)
                }
            },
            confirmButton = {
                Button(onClick = { showDetailMuelleDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    if (showDetailPuertoDialog && selectedPuertoForDetail != null) {
        AlertDialog(
            onDismissRequest = { showDetailPuertoDialog = false },
            title = { Text(text = selectedPuertoForDetail!!.nombre, fontWeight = FontWeight.Black, color = PrimaryBlue) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailFieldMuelle("ID de Puerto en Sistema", selectedPuertoForDetail!!.id.toString())
                    DetailFieldMuelle("Ciudad", selectedPuertoForDetail!!.ciudad)
                    DetailFieldMuelle("Capacidad Máxima", "${selectedPuertoForDetail!!.capacidadMaximaBuques} buques")
                    DetailFieldMuelle("Estado del Puerto", selectedPuertoForDetail!!.estado.uppercase())
                }
            },
            confirmButton = {
                Button(onClick = { showDetailPuertoDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun DetailFieldMuelle(label: String, value: String) {
    Column {
        Text(text = label.uppercase(), fontSize = 9.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 15.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MuelleCardItem(
    muelle: MuelleDto,
    atraque: AtraqueDto?,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val estadoLower = muelle.estado.lowercase()
    val (statusLabel, statusColor) = when {
        estadoLower == "activo" && atraque == null -> "Disponible" to VerdeEsmeralda
        estadoLower == "inactivo" -> "Mantenimiento" to RojoCoral
        else -> "Ocupado" to AzulAcero
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

            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Anchor,
                            contentDescription = null,
                            tint = AzulAbisal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Muelle ${muelle.codigo}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentWidth()) {
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
                    text = "Capacidad de atraque: ${muelle.capacidadAtraque} buques | Puerto: ${muelle.puerto.nombre}",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                            tint = AzulAcero,
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
}

@Composable
fun PuertoCardItem(
    puerto: PuertoDto,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val (statusLabel, statusColor) = if (puerto.estado.lowercase() == "activo") {
        "Activo" to VerdeEsmeralda
    } else {
        "Inactivo" to RojoCoral
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

            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
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
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentWidth()) {
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
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuelleFormDialog(
    muelle: MuelleDto?,
    puertos: List<PuertoDto>,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, Int) -> Unit
) {
    var codigo by remember { mutableStateOf(muelle?.codigo ?: "") }
    var capacidadText by remember { mutableStateOf(muelle?.capacidadAtraque?.toString() ?: "") }
    var estado by remember { mutableStateOf(muelle?.estado?.lowercase() ?: "activo") }
    var puertoIdSelected by remember { mutableIntStateOf(muelle?.puerto?.id ?: puertos.firstOrNull()?.id ?: 0) }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var estadoDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (muelle == null) "Nuevo Muelle" else "Editar Muelle", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (errorMessage != null) {
                    Surface(
                        color = RojoCoral.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, RojoCoral.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = RojoCoral,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código") })
                OutlinedTextField(value = capacidadText, onValueChange = { capacidadText = it }, label = { Text("Capacidad") })

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = { estadoDropdownExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                        val label = when (estado.lowercase()) {
                            "activo" -> "Activo"
                            "inactivo" -> "Inactivo"
                            else -> estado
                        }
                        Text("Estado: $label")
                    }
                    DropdownMenu(expanded = estadoDropdownExpanded, onDismissRequest = { estadoDropdownExpanded = false }) {
                        listOf("activo", "inactivo").forEach { est ->
                            val label = when (est.lowercase()) {
                                "activo" -> "Activo"
                                "inactivo" -> "Inactivo"
                                else -> est
                            }
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    estado = est
                                    estadoDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

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
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, String) -> Unit
) {
    var nombre by remember { mutableStateOf(puerto?.nombre ?: "") }
    var ciudad by remember { mutableStateOf(puerto?.ciudad ?: "") }
    var capMaxText by remember { mutableStateOf(puerto?.capacidadMaximaBuques?.toString() ?: "") }
    var estado by remember { mutableStateOf(puerto?.estado?.lowercase() ?: "activo") }
    var estadoDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (puerto == null) "Nuevo Puerto" else "Editar Puerto", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (errorMessage != null) {
                    Surface(
                        color = RojoCoral.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, RojoCoral.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = RojoCoral,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") })
                OutlinedTextField(value = capMaxText, onValueChange = { capMaxText = it }, label = { Text("Capacidad Máxima") })

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = { estadoDropdownExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                        val label = when (estado.lowercase()) {
                            "activo" -> "Activo"
                            "inactivo" -> "Inactivo"
                            else -> estado
                        }
                        Text("Estado: $label")
                    }
                    DropdownMenu(expanded = estadoDropdownExpanded, onDismissRequest = { estadoDropdownExpanded = false }) {
                        listOf("activo", "inactivo").forEach { est ->
                            val label = when (est) {
                                "activo" -> "Activo"
                                "inactivo" -> "Inactivo"
                                else -> est
                            }
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    estado = est
                                    estadoDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
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