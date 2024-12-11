package com.example.clock.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Green500 = Color(0xFF4CAF50)
private val Green700 = Color(0xFF388E3C)
private val Green200 = Color(0xFFC8E6C9)

private val LightColors = lightColorScheme(
    primary = Green500,
    onPrimary = Color.White,
    secondary = Green700,
    onSecondary = Color.White,
    background = Green200,
    onBackground = Color.Black,
    surface = Green200,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White,
)

@Composable
fun ClockInTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}