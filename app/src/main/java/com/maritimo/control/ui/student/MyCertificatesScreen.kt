package com.maritimo.control.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MyCertificatesScreen(
    onBack: () -> Unit,
    viewModel: MuelleAllocationViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Certificados Marítimos de Operadores", style = MaterialTheme.typography.headlineMedium)
    }
}