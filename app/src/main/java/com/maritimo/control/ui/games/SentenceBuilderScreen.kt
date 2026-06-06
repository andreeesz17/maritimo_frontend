package com.maritimo.control.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SentenceBuilderScreen(onBack: () -> Unit, viewModel: SentenceBuilderViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Constructor de Protocolos de Emergencia", style = MaterialTheme.typography.headlineMedium)
    }
}