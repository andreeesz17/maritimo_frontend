package com.maritimo.control.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maritimo.control.ui.viewmodel.OrdersViewModel

@Composable
fun OrdersScreen(viewModel: OrdersViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Historial de Atraques y Pedidos", style = MaterialTheme.typography.headlineMedium)
    }
}