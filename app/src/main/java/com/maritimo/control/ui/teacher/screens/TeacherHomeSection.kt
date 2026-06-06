package com.maritimo.control.ui.teacher.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maritimo.control.ui.teacher.TeacherMainViewModel

@Composable
fun TeacherHomeSection(viewModel: TeacherMainViewModel) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Panel de Operaciones de Muelles", style = MaterialTheme.typography.headlineMedium)
    }
}