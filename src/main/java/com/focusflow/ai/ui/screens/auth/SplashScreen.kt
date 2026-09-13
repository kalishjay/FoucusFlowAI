package com.focusflow.ai.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusflow.ai.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Pulse animation for holographic orb
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Animated boot progress (0% -> 100%)
    var bootProgress by remember { mutableFloatStateOf(0f) }
    var bootStatusText by remember { mutableStateOf("INITIALIZING NEURAL CORE...") }

    val animatedBootProgress by animateFloatAsState(
        targetValue = bootProgress,
        animationSpec = tween(2800, easing = LinearOutSlowInEasing),
        label = "bootProgress"
    )

    LaunchedEffect(Unit) {
        bootProgress = 1f

        delay(700L)
        bootStatusText = "CONNECTING SQLite ROOM PERSISTENCE..."

        delay(900L)
        bootStatusText = "SYNCHRONIZING GEMINI AI MATRIX..."

        delay(800L)
        bootStatusText = "COGNITIVE FLOW PROTOCOL: ONLINE"

        delay(800L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF070614),
                        Color(0xFF100E29),
                        Color(0xFF1C1744)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 28.dp)
        ) {
            // Futuristic Glowing Holographic Reactor Core
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(136.dp)
                    .scale(scale)
            ) {
                // Outer Cyan-Violet Neon Ring
                Surface(
                    shape = CircleShape,
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.5.dp, CyberNeonGradient, CircleShape)
                ) {}

                // Middle Translucent Ambient Halo
                Surface(
                    shape = CircleShape,
                    color = NeonViolet.copy(alpha = 0.18f),
                    modifier = Modifier.size(112.dp)
                ) {}

                // Inner AI Core
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF141130),
                    modifier = Modifier
                        .size(86.dp)
                        .border(1.5.dp, NeonCyan, CircleShape)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Core",
                            tint = NeonCyan,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Brand Name with Glowing Neon Letter Spacing
            Text(
                text = "FocusFlow AI",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    fontSize = 34.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle Tagline
            Text(
                text = "YOUR DAY AS A FLOW, NOT A LIST",
                color = AccentCoral,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // High-Tech Boot Progress Bar
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { animatedBootProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NeonCyan,
                    trackColor = CyberCardElevated
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Realtime Telemetry Status Text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bootStatusText,
                        color = TextCyberMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${(animatedBootProgress * 100).toInt()}%",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bottom App Version
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CyberCardSurface,
                modifier = Modifier.border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "FOCUSFLOW AI • v1.0 NEURAL CORE",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
    }
}