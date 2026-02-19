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
    primaryContainer = PrimaryGreenLight.copy(alpha = 0.3f), // Softer container
    onPrimaryContainer = PrimaryGreenDark,
    secondary = SecondaryTeal,
    onSecondary = TextOnPrimary,
    secondaryContainer = SecondaryTeal.copy(alpha = 0.2f),
    onSecondaryContainer = SecondaryTealDark,
    tertiary = TertiaryPurple,
    onTertiary = TextOnPrimary,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFE2E8F0), // Slate 200
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
    error = StatusMissed,
    onError = TextOnPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen, // Keep vibrant even in dark mode
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryGreenDark,
    onPrimaryContainer = TextOnPrimary,
    secondary = SecondaryTealLight,
    onSecondary = TextPrimary,
    secondaryContainer = SecondaryTealDark,
    onSecondaryContainer = SecondaryTealLight,
    tertiary = TertiaryPurple,
    onTertiary = TextOnPrimary,
    background = BackgroundDark,
    onBackground = TextOnPrimary,
    surface = SurfaceDark,
    onSurface = TextOnPrimary,
    surfaceVariant = CardDark,
    onSurfaceVariant = Color(0xFF94A3B8), // Slate 400
    outline = Color(0xFF475569), // Slate 600
    error = Color(0xFFF87171), // Red 400
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
