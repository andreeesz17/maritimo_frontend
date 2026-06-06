package com.maritimo.control.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maritimo.control.ui.theme.*
import com.maritimo.control.ui.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    gamificationViewModel: com.maritimo.control.ui.viewmodel.GamificationViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onLogout: () -> Unit,
    onNavigateToPremium: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToAchievements: () -> Unit = {},
    onNavigateToCertificates: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToCapitanes: () -> Unit = {}
) {
    val user by authViewModel.currentUser.collectAsState()
    val isAdmin by authViewModel.isAdminRole.collectAsState()

    val buquesCount by gamificationViewModel.buquesCount.collectAsState()
    val atraquesCount by gamificationViewModel.atraquesCount.collectAsState()
    val inspeccionesCount by gamificationViewModel.inspeccionesCount.collectAsState()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            AzulAbisal,
            Color(0xFF0F1B35),
            Color(0xFF132A50)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 450.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    BorderStroke(1.5.dp, Color.White.copy(alpha = 0.12f)),
                    RoundedCornerShape(28.dp)
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0C162A).copy(alpha = 0.75f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Avatar con borde gradiente brillante y efecto de pulido
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(CianElectrico, AzulAcero)))
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF070D19)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (user?.username?.firstOrNull()?.toString() ?: "O").uppercase(),
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Información del Usuario
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = user?.username ?: "Operador",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user?.email ?: "correo@puerto.com",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Badge de Rol con diseño tecnológico
                val statusColor = if (isAdmin) VerdeEsmeralda else AzulAcero
                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = if (isAdmin) "Administrador / Staff" else "Operador de Consulta",
                            color = statusColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 1.dp)

                // Mini-Stats Section (Métricas)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(16.dp))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stat 1: Atraques Activos
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Anchor,
                            contentDescription = null,
                            tint = CianElectrico,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = atraquesCount.toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Atraques", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                    }

                    // Divisor
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.White.copy(alpha = 0.08f)))

                    // Stat 2: Inspecciones de Seguridad
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = VerdeEsmeralda,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = inspeccionesCount.toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Inspecciones", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                    }

                    // Divisor
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.White.copy(alpha = 0.08f)))

                    // Stat 3: Puertos Activos
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBoat,
                            contentDescription = null,
                            tint = AmbarAlerta,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = buquesCount.toString(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Buques", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 1.dp)

                // Menu items / Action buttons
                Button(
                    onClick = onNavigateToCapitanes,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.linearGradient(listOf(AzulAcero, CianElectrico)))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupervisorAccount,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gestión de Capitanes",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, RojoCoral.copy(alpha = 0.8f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = RojoCoral.copy(alpha = 0.08f),
                        contentColor = RojoCoral
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = RojoCoral,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}