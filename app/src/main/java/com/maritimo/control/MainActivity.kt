package com.maritimo.control

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.maritimo.control.ui.components.LoadingScreen
import com.maritimo.control.navigation.Screen
import com.maritimo.control.ui.auth.LoginScreen
import com.maritimo.control.ui.auth.RegisterScreen
import com.maritimo.control.ui.home.HomeScreen
import com.maritimo.control.ui.home.CatalogScreen
import com.maritimo.control.ui.navigation.BottomNavBar
import com.maritimo.control.ui.viewmodel.AuthViewModel
import com.maritimo.control.ui.viewmodel.CartViewModel
import com.maritimo.control.ui.viewmodel.HomeViewModel
import com.maritimo.control.ui.theme.ControlMaritimoTheme
import dagger.hilt.android.AndroidEntryPoint

import com.maritimo.control.ui.navigation.NavGraph

import androidx.activity.SystemBarStyle

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            ControlMaritimoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val authViewModel: AuthViewModel = hiltViewModel()
                    NavGraph(authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun AdminDashboardTestScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Panel de Administración", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onLogout) { Text("Cerrar sesión") }
    }
}
