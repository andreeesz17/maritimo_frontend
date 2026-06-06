package com.maritimo.control.ui.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maritimo.control.data.remote.dto.CapitanDto
import com.maritimo.control.ui.theme.*
import com.maritimo.control.ui.viewmodel.AuthViewModel
import com.maritimo.control.ui.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onCourseClick: (Int) -> Unit,
    onOpenCart: () -> Unit,
    cartViewModel: com.maritimo.control.ui.viewmodel.CartViewModel,
    viewModel: CatalogViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    authViewModel: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isAdmin by authViewModel.isAdminRole.collectAsState()

    var showCreateEditDialog by remember { mutableStateOf(false) }
    var selectedCapitanForEdit by remember { mutableStateOf<CapitanDto?>(null) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedCapitanForDetail by remember { mutableStateOf<CapitanDto?>(null) }

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
                            imageVector = Icons.Default.SupervisorAccount,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Control de Capitanes",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadCatalog() }) {
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
                        selectedCapitanForEdit = null
                        showCreateEditDialog = true
                    },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Capitán")
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

            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Buscar capitán por nombre...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // list container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading && state.capitanes.isEmpty()) {
                    CircularProgressIndicator(color = PrimaryBlue)
                } else if (state.capitanes.isEmpty()) {
                    Text("No se encontraron capitanes registrados.", color = TextSecondary)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.capitanes, key = { it.id }) { capitan ->
                            CapitanCardItem(
                                capitan = capitan,
                                isAdmin = isAdmin,
                                onClick = {
                                    selectedCapitanForDetail = capitan
                                    showDetailDialog = true
                                },
                                onEdit = {
                                    selectedCapitanForEdit = capitan
                                    showCreateEditDialog = true
                                },
                                onDelete = {
                                    viewModel.deleteCapitan(capitan.id)
                                }
                            )
                        }

                        if (state.hasMore) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    Button(
                                        onClick = { viewModel.loadMoreCapitanes() },
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

    // Dialogo crear/editar
    if (showCreateEditDialog) {
        CreateEditCapitanDialog(
            capitan = selectedCapitanForEdit,
            onDismiss = { showCreateEditDialog = false },
            onConfirm = { nombres, apellidos, licencia, nacionalidad ->
                val newCap = CapitanDto(
                    id = selectedCapitanForEdit?.id ?: 0,
                    nombres = nombres,
                    apellidos = apellidos,
                    licenciaNavegacion = licencia,
                    nacionalidad = nacionalidad
                )
                if (selectedCapitanForEdit == null) {
                    viewModel.createCapitan(newCap) { showCreateEditDialog = false }
                } else {
                    viewModel.updateCapitan(newCap.id, newCap) { showCreateEditDialog = false }
                }
            }
        )
    }

    // Dialogo detalle
    if (showDetailDialog && selectedCapitanForDetail != null) {
        CapitanDetailDialog(
            capitan = selectedCapitanForDetail!!,
            onDismiss = { showDetailDialog = false }
        )
    }
}

@Composable
fun CapitanCardItem(
    capitan: CapitanDto,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = capitan.nombres.firstOrNull()?.toString() ?: "",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${capitan.nombres} ${capitan.apellidos}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Licencia: ${capitan.licenciaNavegacion} | ${capitan.nacionalidad}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            if (isAdmin) {
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AccentBlue, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = ErrorColor, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditCapitanDialog(
    capitan: CapitanDto?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var nombres by remember { mutableStateOf(capitan?.nombres ?: "") }
    var apellidos by remember { mutableStateOf(capitan?.apellidos ?: "") }
    var licencia by remember { mutableStateOf(capitan?.licenciaNavegacion ?: "") }
    var nacionalidad by remember { mutableStateOf(capitan?.nacionalidad ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (capitan == null) "Nuevo Capitán" else "Editar Capitán", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nombres, onValueChange = { nombres = it }, label = { Text("Nombres") })
                OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") })
                OutlinedTextField(value = licencia, onValueChange = { licencia = it }, label = { Text("Licencia de Navegación") })
                OutlinedTextField(value = nacionalidad, onValueChange = { nacionalidad = it }, label = { Text("Nacionalidad") })
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombres, apellidos, licencia, nacionalidad) },
                enabled = nombres.isNotBlank() && apellidos.isNotBlank() && licencia.isNotBlank()
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
fun CapitanDetailDialog(
    capitan: CapitanDto,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "${capitan.nombres} ${capitan.apellidos}", fontWeight = FontWeight.Black, color = PrimaryBlue) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailField("Licencia Registrada", capitan.licenciaNavegacion)
                DetailField("Nacionalidad", capitan.nacionalidad)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}