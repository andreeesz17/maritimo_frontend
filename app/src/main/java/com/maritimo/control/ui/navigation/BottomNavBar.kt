package com.maritimo.control.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.maritimo.control.navigation.Screen
import com.maritimo.control.ui.theme.*

data class BottomNavItem(
    val screen:       Screen?,
    val label:        String,
    val icon:         ImageVector,
    val iconSelected: ImageVector,
    val badgeCount:   Int = 0,
    val onClick:      (() -> Unit)? = null
)

@Composable
fun BottomNavBar(
    navController: NavController,
) {
    val items = listOf(
        BottomNavItem(Screen.Home,       "Buques",       Icons.Outlined.DirectionsBoat, Icons.Filled.DirectionsBoat),
        BottomNavItem(Screen.MyClasses,  "Muelles",      Icons.Outlined.Anchor,         Icons.Filled.Anchor),
        BottomNavItem(Screen.GameCenter, "Inspecciones", Icons.Outlined.Security,       Icons.Filled.Security),
        BottomNavItem(Screen.Profile,    "Perfil",       Icons.Outlined.AccountCircle,  Icons.Filled.AccountCircle),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute      = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp) // Más margen lateral para flotado premium
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = AzulAbisal.copy(alpha = 0.90f),
            shadowElevation = 16.dp,
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        1.5.dp,
                        Brush.linearGradient(
                            colors = listOf(
                                CianElectrico.copy(alpha = 0.35f),
                                AzulAcero.copy(alpha = 0.1f)
                            )
                        )
                    ),
                    RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = if (item.screen != null) {
                        currentRoute?.startsWith(item.screen.route.substringBefore("/{")) == true
                    } else false

                    val activeColor = CianElectrico
                    val inactiveColor = Color.White.copy(alpha = 0.5f)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (item.onClick != null) {
                                    item.onClick.invoke()
                                } else if (item.screen != null) {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Contenedor del ícono con un pad brillante de fondo cuando está activo
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) CianElectrico.copy(alpha = 0.12f) else Color.Transparent)
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(contentAlignment = Alignment.TopEnd) {
                                    Icon(
                                        imageVector = if (isSelected) item.iconSelected else item.icon,
                                        contentDescription = item.label,
                                        tint = if (isSelected) activeColor else inactiveColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    if (item.badgeCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(RojoCoral, CircleShape)
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = item.label,
                                color = if (isSelected) Color.White else inactiveColor,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
