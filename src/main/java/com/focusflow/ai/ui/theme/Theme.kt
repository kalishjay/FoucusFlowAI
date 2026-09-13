package com.focusflow.ai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = FlowIndigo,
    onPrimary = SurfaceCard,
    primaryContainer = FlowVioletLight,
    onPrimaryContainer = FlowIndigo,
    secondary = FlowViolet,
    onSecondary = SurfaceCard,
    tertiary = AccentCoral,
    onTertiary = SurfaceCard,
    tertiaryContainer = AccentCoralLight,
    onTertiaryContainer = AccentCoralHover,
    background = SoftBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor
)

private val DarkColorScheme = darkColorScheme(
    primary = NeonViolet,
    onPrimary = TextCyberWhite,
    primaryContainer = FlowIndigoDark,
    onPrimaryContainer = TextCyberWhite,
    secondary = NeonCyan,
    onSecondary = CyberDarkBg,
    tertiary = AccentCoral,
    onTertiary = TextCyberWhite,
    background = CyberDarkBg,
    onBackground = TextCyberWhite,
    surface = CyberDarkSurface,
    onSurface = TextCyberWhite,
    surfaceVariant = CyberCardSurface,
    onSurfaceVariant = TextCyberMuted,
    outline = CyberCardBorder
)

@Composable
fun FocusFlowAITheme(
    darkTheme: Boolean = true, // Default to futuristic dark aesthetic
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