package com.maritimo.control.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.maritimo.control.ui.theme.*

@Composable
fun MaritimoTextField(
    value:         String,
    onValueChange: (String) -> Unit,
    label:         String,
    modifier:      Modifier = Modifier,
    placeholder:   String   = "",
    isError:       Boolean  = false,
    errorMessage:  String?  = null,
    isPassword:    Boolean  = false,
    trailingIcon:  @Composable (() -> Unit)? = null,
    keyboardType:  KeyboardType = KeyboardType.Text,
    imeAction:     ImeAction    = ImeAction.Next,
    enabled:       Boolean      = true,
    isDarkTheme:   Boolean      = false,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = when {
        isPassword && !passwordVisible -> PasswordVisualTransformation()
        else                           -> VisualTransformation.None
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value           = value,
            onValueChange   = onValueChange,
            label           = { Text(label, style = MaterialTheme.typography.labelLarge) },
            placeholder     = { Text(placeholder, color = if (isDarkTheme) TextFaintTech else TextFaint, style = MaterialTheme.typography.bodyMedium) },
            textStyle       = MaterialTheme.typography.bodyMedium,
            isError         = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            enabled         = enabled,
            singleLine      = true,
            modifier        = Modifier.fillMaxWidth(),
            shape           = RoundedCornerShape(14.dp),
            colors          = OutlinedTextFieldDefaults.colors(
                focusedTextColor       = if (isDarkTheme) Color.White else TextPrimary,
                unfocusedTextColor     = if (isDarkTheme) Color.White else TextPrimary,
                focusedBorderColor     = if (isDarkTheme) ElectricCyan else PrimaryBlue,
                focusedLabelColor      = if (isDarkTheme) ElectricCyan else PrimaryBlue,
                cursorColor            = if (isDarkTheme) ElectricCyan else PrimaryBlue,
                unfocusedBorderColor   = if (isDarkTheme) GlassBorder else Border,
                unfocusedLabelColor    = if (isDarkTheme) TextFaintTech else TextSecondary,
                errorBorderColor       = ErrorColor,
                errorLabelColor        = ErrorColor,
                focusedContainerColor  = if (isDarkTheme) DarkSurface.copy(alpha = 0.6f) else SurfaceColor,
                unfocusedContainerColor = if (isDarkTheme) DarkSurface.copy(alpha = 0.4f) else SurfaceColor,
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                            tint = if (isDarkTheme) TextFaintTech else TextSecondary,
                        )
                    }
                }
            } else trailingIcon,
        )
        if (isError && errorMessage != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text  = errorMessage,
                color = ErrorColor,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
