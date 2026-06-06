package com.maritimo.control.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maritimo.control.ui.theme.PrimaryBlue

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    onNavigateToUsers: () -> Unit = {},
    onNavigateToCourses: () -> Unit = {},
    onNavigateToRoles: () -> Unit = {},
    onNavigateToAdminOrders: () -> Unit = {},
    onNavigateToAuditLogs: () -> Unit = {},
    viewModel: AdminMainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Panel de Operador de Puerto (Dashboard)",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLogout) {
                Text("Cerrar Sesión")
            }
        }
    }
}
