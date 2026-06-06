package com.maritimo.control.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maritimo.control.ui.viewmodel.CourseDetailViewModel

@Composable
fun CourseDetailScreen(
    courseId: Int,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    cartViewModel: com.maritimo.control.ui.viewmodel.CartViewModel,
    viewModel: CourseDetailViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Detalle de Buque y Capacidad de Muelle", style = MaterialTheme.typography.headlineMedium)
    }
}