package com.example.clock.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Green80,
    onPrimary = Color.White,
    secondary = Green60,
    onSecondary = Color.White,
    background = Green20,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = DarkGreen80,
    onPrimary = Color.White,
    secondary = DarkGreen60,
    onSecondary = Color.White,
    background = DarkGreen20,
    onBackground = Color.White,
    surface = DarkGreen40,
    onSurface = Color.White
)

@Composable
fun ClockInAppTheme(
    darkTheme: Boolean = true, // Set to true for dark theme by default
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}