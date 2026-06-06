package com.maritimo.control.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var pendingDeleteId by remember { mutableStateOf<Int?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            AzulAbisal,
            Color(0xFF09142E),
            Color(0xFF060B13)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        // Resplandor radial de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CianElectrico.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        radius = 600f
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                // Header inmersivo premium
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    AzulAbisal,
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        // Título con icono
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(AzulAcero, CianElectrico)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SupervisorAccount,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Control de Capitanes",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "${state.capitanes.size} registros activos",
                                    fontSize = 12.sp,
                                    color = CianElectrico.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            // Botón refresh
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.06f))
                                    .clickable { viewModel.loadCatalog() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Recargar",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Barra de búsqueda premium
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1B2A4A).copy(alpha = 0.4f))
                                .border(1.dp, CianElectrico.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = CianElectrico,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            BasicTextField(
                                value = state.searchQuery,
                                onValueChange = { viewModel.updateSearchQuery(it) },
                                singleLine = true,
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = Color.White,
                                    fontSize = 15.sp
                                ),
                                modifier = Modifier.weight(1f),
                                decorationBox = { innerTextField ->
                                    if (state.searchQuery.isEmpty()) {
                                        Text(
                                            "Buscar capitán por nombre...",
                                            color = Color.White.copy(alpha = 0.35f),
                                            fontSize = 15.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                            if (state.searchQuery.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar",
                                    tint = Color.White.copy(alpha = 0.4f),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { viewModel.updateSearchQuery("") }
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                if (isAdmin) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(listOf(AzulAcero, CianElectrico))
                            )
                            .clickable {
                                selectedCapitanForEdit = null
                                showCreateEditDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Añadir Capitán",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    state.isLoading && state.capitanes.isEmpty() -> {
                        // Loader premium
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                color = CianElectrico,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Cargando registros...",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        }
                    }
                    state.capitanes.isEmpty() -> {
                        // Estado vacío premium
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(CianElectrico.copy(alpha = 0.08f))
                                    .border(1.dp, CianElectrico.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SupervisorAccount,
                                    contentDescription = null,
                                    tint = CianElectrico.copy(alpha = 0.5f),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Text(
                                text = "Sin capitanes registrados",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Agrega el primer capitán usando el botón +",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
                        ) {
                            // Contador de resultados
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (state.searchQuery.isNotEmpty())
                                            "Resultados para \"${state.searchQuery}\""
                                        else "Todos los capitanes",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Surface(
                                        color = CianElectrico.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, CianElectrico.copy(alpha = 0.25f))
                                    ) {
                                        Text(
                                            text = "${state.capitanes.size}",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = CianElectrico
                                        )
                                    }
                                }
                            }

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
                                        pendingDeleteId = capitan.id
                                        showDeleteConfirmDialog = true
                                    }
                                )
                            }

                            if (state.hasMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Button(
                                            onClick = { viewModel.loadMoreCapitanes() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent
                                            ),
                                            contentPadding = PaddingValues(),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    Brush.linearGradient(listOf(AzulAcero, CianElectrico))
                                                )
                                        ) {
                                            Text(
                                                "Cargar más",
                                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
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

    // Dialogo confirmar eliminación
    if (showDeleteConfirmDialog && pendingDeleteId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text(
                    "Eliminar Capitán",
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    "¿Estás seguro de que quieres eliminar este capitán? Esta acción no se puede deshacer.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        pendingDeleteId?.let { viewModel.deleteCapitan(it) }
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

// Función auxiliar para generar colores de avatar según inicial
private fun getAvatarColorForLetter(letter: Char): Brush {
    return when (letter.lowercaseChar()) {
        in 'a'..'e' -> Brush.linearGradient(listOf(Color(0xFF007EA7), Color(0xFF00A8E8)))
        in 'f'..'j' -> Brush.linearGradient(listOf(Color(0xFF2EC4B6), Color(0xFF00A8E8)))
        in 'k'..'o' -> Brush.linearGradient(listOf(Color(0xFF0A1128), Color(0xFF007EA7)))
        in 'p'..'t' -> Brush.linearGradient(listOf(Color(0xFFFF9F1C), Color(0xFFE71D36)))
        else        -> Brush.linearGradient(listOf(Color(0xFF007EA7), Color(0xFF2EC4B6)))
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
    val initial = capitan.nombres.firstOrNull() ?: 'C'
    val avatarGradient = getAvatarColorForLetter(initial)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .border(
                BorderStroke(1.dp, Color.White.copy(alpha = 0.07f)),
                RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0D1B30).copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con inicial y gradiente dinámico
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(avatarGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial.uppercaseChar().toString(),
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info del capitán
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${capitan.nombres} ${capitan.apellidos}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Badge de licencia
                    Surface(
                        color = AzulAcero.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, AzulAcero.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = CianElectrico,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = capitan.licenciaNavegacion,
                                fontSize = 10.sp,
                                color = CianElectrico,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    // Badge de nacionalidad
                    Surface(
                        color = VerdeEsmeralda.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, VerdeEsmeralda.copy(alpha = 0.25f))
                    ) {
                        Text(
                            text = capitan.nacionalidad,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            fontSize = 10.sp,
                            color = VerdeEsmeralda,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Acciones de admin
            if (isAdmin) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AzulAcero.copy(alpha = 0.12f))
                            .clickable { onEdit() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = CianElectrico,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(RojoCoral.copy(alpha = 0.10f))
                            .clickable { onDelete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = RojoCoral,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            } else {
                // Icono de flecha para ver detalles
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier.size(20.dp)
                )
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
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(listOf(AzulAcero, CianElectrico))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (capitan == null) Icons.Default.PersonAdd else Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = if (capitan == null) "Nuevo Capitán" else "Editar Capitán",
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 19.sp
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = CianElectrico,
                    focusedLabelColor = CianElectrico,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = CianElectrico,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                    focusedContainerColor = Color(0xFF1B2A4A).copy(alpha = 0.25f),
                    unfocusedContainerColor = Color(0xFF1B2A4A).copy(alpha = 0.12f)
                )

                PremiumDialogField(
                    value = nombres,
                    onValueChange = { nombres = it },
                    label = "Nombres",
                    icon = Icons.Default.Person,
                    colors = textFieldColors
                )
                PremiumDialogField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = "Apellidos",
                    icon = Icons.Default.Person,
                    colors = textFieldColors
                )
                PremiumDialogField(
                    value = licencia,
                    onValueChange = { licencia = it },
                    label = "Licencia de Navegación",
                    icon = Icons.Default.VerifiedUser,
                    colors = textFieldColors
                )
                PremiumDialogField(
                    value = nacionalidad,
                    onValueChange = { nacionalidad = it },
                    label = "Nacionalidad",
                    icon = Icons.Default.Flag,
                    colors = textFieldColors
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombres, apellidos, licencia, nacionalidad) },
                enabled = nombres.isNotBlank() && apellidos.isNotBlank() && licencia.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color(0xFF1B2A4A)
                ),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            if (nombres.isNotBlank() && apellidos.isNotBlank() && licencia.isNotBlank())
                                Brush.linearGradient(listOf(AzulAcero, CianElectrico))
                            else
                                Brush.linearGradient(listOf(Color(0xFF1B2A4A), Color(0xFF1B2A4A)))
                        )
                        .padding(horizontal = 20.dp, vertical = 11.dp)
                ) {
                    Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.White.copy(alpha = 0.5f))
            }
        },
        containerColor = Color(0xFF090F1E),
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun PremiumDialogField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    colors: androidx.compose.material3.TextFieldColors
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CianElectrico.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        },
        colors = colors,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun CapitanDetailDialog(
    capitan: CapitanDto,
    onDismiss: () -> Unit
) {
    val initial = capitan.nombres.firstOrNull() ?: 'C'
    val avatarGradient = getAvatarColorForLetter(initial)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar grande
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(avatarGradient)
                        .border(2.dp, CianElectrico.copy(alpha = 0.3f), RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial.uppercaseChar().toString(),
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${capitan.nombres} ${capitan.apellidos}",
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = CianElectrico.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CianElectrico.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "Capitán de Navegación",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = CianElectrico,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                DetailFieldCapitan(
                    label = "ID en Sistema",
                    value = "#${capitan.id}",
                    icon = Icons.Default.Tag
                )
                DetailFieldCapitan(
                    label = "Licencia de Navegación",
                    value = capitan.licenciaNavegacion,
                    icon = Icons.Default.VerifiedUser
                )
                DetailFieldCapitan(
                    label = "Nacionalidad",
                    value = capitan.nacionalidad,
                    icon = Icons.Default.Flag
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Box(
                    modifier = Modifier
                        .background(Brush.linearGradient(listOf(AzulAcero, CianElectrico)))
                        .padding(horizontal = 24.dp, vertical = 11.dp)
                ) {
                    Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = Color(0xFF090F1E),
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun DetailFieldCapitan(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1B2A4A).copy(alpha = 0.2f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(CianElectrico.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CianElectrico,
                modifier = Modifier.size(15.dp)
            )
        }
        Column {
            Text(
                text = label.uppercase(),
                fontSize = 9.sp,
                color = CianElectrico.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}