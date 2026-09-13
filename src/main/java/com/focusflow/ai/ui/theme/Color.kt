package com.focusflow.ai.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Futuristic 60-30-10 Color System for FocusFlow AI
// Primary (60%) - High-tech Surfaces
val CyberDarkBg = Color(0xFF0B0A1A)
val CyberDarkSurface = Color(0xFF15132B)
val CyberCardSurface = Color(0xFF1D1A3B)
val CyberCardElevated = Color(0xFF26224E)
val CyberCardBorder = Color(0xFF332D68)

// Secondary (30%) - Neural Indigo & Electric Violet
val FlowIndigo = Color(0xFF232159)
val FlowIndigoDark = Color(0xFF0F0E26)
val FlowViolet = Color(0xFF5349B8)
val FlowVioletLight = Color(0xFFEAEAFF)
val NeonViolet = Color(0xFF7B61FF)
val ElectricIndigo = Color(0xFF4A3AFF)

// Accent (10%) - Hyper Glow Accents (AI Core & Active CTAs)
val AccentCoral = Color(0xFFFF6B47)
val AccentCoralHover = Color(0xFFE55836)
val AccentCoralLight = Color(0xFFFFECE6)
val NeonCyan = Color(0xFF00F2FE)
val NeonGreen = Color(0xFF00E676)
val NeonAmber = Color(0xFFFFB300)
val NeonRed = Color(0xFFFF3366)

// Priority Neon Indicators
val PriorityHigh = Color(0xFFFF3366)
val PriorityHighContainer = Color(0xFF3D1627)
val PriorityMedium = Color(0xFFFFB300)
val PriorityMediumContainer = Color(0xFF3D2F14)
val PriorityLow = Color(0xFF00E676)
val PriorityLowContainer = Color(0xFF133624)

// Futuristic Text Colors
val TextCyberWhite = Color(0xFFF6F7FB)
val TextCyberMuted = Color(0xFFA5A8C8)
val TextCyberSubtle = Color(0xFF6E7199)

// Light Theme Backwards Compatibility
val SoftBackground = Color(0xFFF6F7FB)
val SurfaceCard = Color(0xFFFFFFFF)
val SurfaceElevated = Color(0xFFF0F2F8)
val TextPrimary = Color(0xFF16153B)
val TextSecondary = Color(0xFF5C5E7A)
val TextTertiary = Color(0xFF8E90B0)
val DividerColor = Color(0xFFE2E4EB)

// Futuristic Gradients
val CyberNeonGradient = Brush.horizontalGradient(
    listOf(NeonCyan, NeonViolet, AccentCoral)
)

val CyberButtonGradient = Brush.horizontalGradient(
    listOf(NeonViolet, AccentCoral)
)

val CyberAccentGradient = Brush.horizontalGradient(
    listOf(AccentCoral, Color(0xFFFF8E53))
)

val CyberCyanGradient = Brush.horizontalGradient(
    listOf(NeonCyan, NeonViolet)
)

val CyberGreenGradient = Brush.horizontalGradient(
    listOf(NeonGreen, NeonCyan)
)

val CyberCardGlowBorder = Brush.linearGradient(
    listOf(
        NeonViolet.copy(alpha = 0.6f),
        NeonCyan.copy(alpha = 0.4f),
        AccentCoral.copy(alpha = 0.5f)
    )
)

val NeuralCoreGradient = Brush.verticalGradient(
    listOf(
        FlowIndigoDark,
        CyberDarkSurface,
        CyberDarkBg
    )
)