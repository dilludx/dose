package com.dose.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryGreenSoft,
    onPrimaryContainer = PrimaryGreenDark,
    secondary = SecondaryPurple,
    onSecondary = TextOnPrimary,
    secondaryContainer = SecondaryPurpleSoft,
    onSecondaryContainer = SecondaryPurpleDark,
    tertiary = TertiaryAmber,
    onTertiary = TextOnPrimary,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F2F6),
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
    error = StatusMissed,
    onError = TextOnPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryGreenDark,
    onPrimaryContainer = PrimaryGreenLight,
    secondary = SecondaryPurpleLight,
    onSecondary = TextPrimary,
    secondaryContainer = SecondaryPurpleDark,
    onSecondaryContainer = SecondaryPurpleLight,
    tertiary = TertiaryAmber,
    onTertiary = TextPrimary,
    background = BackgroundDark,
    onBackground = Color(0xFFE5E7EB),
    surface = SurfaceDark,
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = CardDark,
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF374151),
    error = Color(0xFFF87171),
    onError = TextPrimary
)

@Composable
fun DoseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
