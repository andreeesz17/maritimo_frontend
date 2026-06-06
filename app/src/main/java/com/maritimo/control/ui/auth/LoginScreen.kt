package com.maritimo.control.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maritimo.control.R
import com.maritimo.control.ui.components.MaritimoTextField
import com.maritimo.control.ui.components.MaritimoButton
import com.maritimo.control.ui.viewmodel.AuthViewModel
import com.maritimo.control.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: (role: String, isStaff: Boolean) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isUsernameValid = remember(username) {
        username.isEmpty() || username.length >= 3
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val user = (uiState as AuthUiState.Success).user
            onLoginSuccess(user.role, user.isStaff)
        }
    }

    val isLoading = uiState is AuthUiState.Loading
    val errorMsg = (uiState as? AuthUiState.Error)?.message

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A1E36), // Azul abisal oscuro
            Color(0xFF134074), // Azul acero real
            Color(0xFF225791)  // Azul claro enérgico
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            // --- CABECERA DE LOGO ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.size(150.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_ecupuerto),
                            contentDescription = "Logo EcuPuerto",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = androidx.compose.ui.text.buildAnnotatedString {
                        append("Ecu")
                        pushStyle(androidx.compose.ui.text.SpanStyle(color = CianElectrico))
                        append("Puerto")
                        pop()
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Sistema Digital de Control y Gestión Portuaria",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        letterSpacing = 0.2.sp
                    ),
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(28.dp))

            // --- TARJETA DARK-THEMED PREMIUM ---
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0C1726).copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                border = BorderStroke(1.dp, GlassBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .widthIn(max = 450.dp)
            ) {
                Column(modifier = Modifier.padding(28.dp)) {
                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Inicia sesión para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CianElectrico
                    )

                    Spacer(Modifier.height(24.dp))

                    // Alerta de Error estilizada
                    if (errorMsg != null) {
                        Surface(
                            color = RojoCoral.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            border = BorderStroke(1.dp, RojoCoral.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = RojoCoral,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = errorMsg,
                                    color = RojoCoral,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    MaritimoTextField(
                        value = username,
                        onValueChange = { username = it; viewModel.clearError() },
                        label = "Nombre de usuario",
                        placeholder = "Ingresa tu usuario",
                        enabled = !isLoading,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        isError = !isUsernameValid,
                        errorMessage = if (!isUsernameValid) "Formato de usuario inválido" else null,
                        isDarkTheme = true
                    )
                    Spacer(Modifier.height(18.dp))

                    MaritimoTextField(
                        value = password,
                        onValueChange = { password = it; viewModel.clearError() },
                        label = "Contraseña",
                        placeholder = "Tu contraseña segura",
                        isPassword = true,
                        enabled = !isLoading,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        isDarkTheme = true
                    )

                    Spacer(Modifier.height(28.dp))

                    MaritimoButton(
                        text = "Iniciar Sesión",
                        onClick = { viewModel.login(username.trim(), password) },
                        isLoading = isLoading,
                        enabled = username.isNotBlank() && password.isNotBlank() && isUsernameValid,
                        useGradient = true
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Footer (Crear Cuenta)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "¿No tienes una cuenta? ",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium,
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Regístrate ahora",
                        color = CianElectrico,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}