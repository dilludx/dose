package com.dose.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryGreenLight,
    onPrimaryContainer = TextPrimary,
    secondary = SecondaryTeal,
    onSecondary = TextOnPrimary,
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = TextPrimary,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFE8F5E9),
    onSurfaceVariant = TextSecondary,
    error = StatusMissed,
    onError = TextOnPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreenLight,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryGreenDark,
    onPrimaryContainer = TextOnPrimary,
    secondary = SecondaryTeal,
    onSecondary = TextOnPrimary,
    secondaryContainer = SecondaryTealDark,
    onSecondaryContainer = TextOnPrimary,
    background = BackgroundDark,
    onBackground = TextOnPrimary,
    surface = SurfaceDark,
    onSurface = TextOnPrimary,
    surfaceVariant = Color(0xFF2E2E2E),
    onSurfaceVariant = Color(0xFFB0B0B0),
    error = Color(0xFFCF6679),
    onError = TextPrimary
)

@Composable
fun DoseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
