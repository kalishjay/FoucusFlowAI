package com.focusflow.ai.ui.screens.focus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusflow.ai.ui.screens.tasks.TaskViewModel
import com.focusflow.ai.ui.theme.*

@Composable
fun FocusTimerScreen(
    focusTimerViewModel: FocusTimerViewModel,
    taskViewModel: TaskViewModel
) {
    val timerState by focusTimerViewModel.timerState.collectAsState()

    val progress = if (timerState.totalSeconds > 0) {
        timerState.remainingSeconds.toFloat() / timerState.totalSeconds
    } else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(500), label = "timerProgress")

    val minutes = timerState.remainingSeconds / 60
    val seconds = timerState.remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuralCoreGradient)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = "Neural Focus Mode",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (timerState.isBreak) "Rest protocol active • Recover cognitive stamina" else "Deep flow state engaged • Minimize distraction",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextCyberMuted)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Active Target Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (timerState.isBreak) NeonGreen else CyberCardBorder, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = CyberCardElevated,
                        modifier = Modifier
                            .size(40.dp)
                            .border(1.dp, if (timerState.isBreak) NeonGreen else AccentCoral, CircleShape)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = null,
                                tint = if (timerState.isBreak) NeonGreen else AccentCoral,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (timerState.isBreak) "CURRENT PROTOCOL:" else "ACTIVE TARGET OBJECTIVE:",
                            style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                        )
                        Text(
                            text = timerState.selectedTaskTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextCyberWhite,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Circular Futuristic Countdown Core
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(270.dp)
                .padding(16.dp)
        ) {
            // Track Ring
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = CyberCardElevated,
                strokeWidth = 14.dp,
                strokeCap = StrokeCap.Round
            )

            // Neon Glowing Progress Ring
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxSize(),
                color = if (timerState.isBreak) NeonGreen else AccentCoral,
                strokeWidth = 14.dp,
                strokeCap = StrokeCap.Round
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 50.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextCyberWhite,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CyberCardElevated,
                    modifier = Modifier.border(1.dp, if (timerState.isBreak) NeonGreen else NeonCyan, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = if (timerState.isBreak) "RECOVERY BREAK" else "DEEP FLOW ACTIVE",
                        color = if (timerState.isBreak) NeonGreen else NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 90.dp)
        ) {
            // Mode Selectors (25m vs 5m)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = !timerState.isBreak,
                    onClick = { focusTimerViewModel.setTimerMode(false) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = null,
                            tint = if (!timerState.isBreak) NeonCyan else TextCyberSubtle,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = { Text("Deep Focus (25m)", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonViolet,
                        selectedLabelColor = Color.White,
                        containerColor = CyberCardSurface,
                        labelColor = TextCyberMuted
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (!timerState.isBreak) NeonCyan else CyberCardBorder,
                        enabled = true,
                        selected = !timerState.isBreak
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    selected = timerState.isBreak,
                    onClick = { focusTimerViewModel.setTimerMode(true) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Coffee,
                            contentDescription = null,
                            tint = if (timerState.isBreak) NeonGreen else TextCyberSubtle,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = { Text("Recovery (5m)", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyberCardElevated,
                        selectedLabelColor = NeonGreen,
                        containerColor = CyberCardSurface,
                        labelColor = TextCyberMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (timerState.isBreak) NeonGreen else CyberCardBorder,
                        enabled = true,
                        selected = timerState.isBreak
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Main Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                IconButton(
                    onClick = { focusTimerViewModel.resetTimer() },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CyberCardSurface)
                        .border(1.2.dp, CyberCardBorder, CircleShape)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = TextCyberMuted, modifier = Modifier.size(24.dp))
                }

                // Play / Pause Button with Cyber Glow Ring
                Button(
                    onClick = { focusTimerViewModel.toggleStartPause() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (timerState.isRunning) NeonViolet else AccentCoral
                    ),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.dp, CyberNeonGradient, CircleShape),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (timerState.isRunning) "Pause" else "Engage",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Log Session to Database Button
                IconButton(
                    onClick = {
                        val minsLogged = (timerState.totalSeconds - timerState.remainingSeconds) / 60
                        val duration = if (minsLogged > 0) minsLogged else 25
                        taskViewModel.logFocusSession(null, timerState.selectedTaskTitle, duration)
                        focusTimerViewModel.resetTimer()
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CyberCardSurface)
                        .border(1.2.dp, NeonGreen.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Save to DB", tint = NeonGreen, modifier = Modifier.size(26.dp))
                }
            }
        }
    }
}