package com.example.summerproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray

private val DarkColorPalette = darkColors(
    onSurface = White,
    surface = Black,
    onBackground = White,
    background = Black,
    onSecondary = LightBlue50,
    secondary = LightBlue800,
    onPrimary = LightBlue50,
    primary = LightBlue700,

)

private val LightColorPalette = lightColors(
    onSurface = Black,
    surface = White,
    onBackground = Black,
    background = White,
    onSecondary = LightBlue50,
    secondary = LightBlue800,
    onPrimary = LightBlue50,
    primary = LightBlue700,
)

@Composable
fun SummerProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}