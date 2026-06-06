package com.maritimo.control.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LearningPathScreen(courseId: Int, courseTitle: String, onBack: () -> Unit, onExerciseClick: (Int) -> Unit, viewModel: LearningPathViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Ruta de Atraque y Descarga", style = MaterialTheme.typography.headlineMedium)
    }
}