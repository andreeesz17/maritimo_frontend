package com.maritimo.control.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun JoinClassScreen(onBack: () -> Unit, onJoinSuccess: (Int) -> Unit, viewModel: JoinClassViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Asociar Operador a Muelle", style = MaterialTheme.typography.headlineMedium)
    }
}