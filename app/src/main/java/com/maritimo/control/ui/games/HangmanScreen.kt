package com.maritimo.control.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HangmanScreen(onBack: () -> Unit, viewModel: HangmanViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ahorcado - Vocabulario Marítimo", style = MaterialTheme.typography.headlineMedium)
    }
}