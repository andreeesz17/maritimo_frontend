package com.maritimo.control.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TeacherDashboardScreen(onLogout: () -> Unit, viewModel: TeacherMainViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Panel de Capitanes y Operadores de Puerto", style = MaterialTheme.typography.headlineMedium)
    }
}