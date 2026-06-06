package com.maritimo.control.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CartBottomSheet(
    cartViewModel: com.maritimo.control.ui.viewmodel.CartViewModel,
    isAuthenticated: Boolean,
    onDismiss: () -> Unit,
    onLoginRequired: () -> Unit,
    onOrderSuccess: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Carrito de Compras / Reserva de Atraques")
    }
}
