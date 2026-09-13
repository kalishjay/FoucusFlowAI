package com.focusflow.ai.ui.screens.planner

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusflow.ai.data.ai.AIScheduleBlock
import com.focusflow.ai.ui.screens.tasks.TaskViewModel
import com.focusflow.ai.ui.theme.*

@Composable
fun AIPlannerScreen(
    taskViewModel: TaskViewModel,
    onNavigateToFocus: (String) -> Unit
) {
    val uiState by taskViewModel.uiState.collectAsState()
    val pendingTasks by taskViewModel.pendingTasks.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.dailyPlan == null) {
            taskViewModel.generateDailyPlan()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuralCoreGradient)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "AI Schedule Matrix",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            )
            Text(
                text = "Dynamic neural scheduling: balances deep focus and cognitive recovery",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextCyberMuted)
            )
        }

        // Generate / Regenerate AI Plan Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FlowIndigoDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, CyberCardGlowBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(CyberCardElevated)
                                    .border(1.dp, NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Smart Flow Engine",
                                    color = TextCyberWhite,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${pendingTasks.size} tasks in queue to optimize",
                                    color = TextCyberMuted,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Button(
                            onClick = { taskViewModel.generateDailyPlan() },
                            enabled = !uiState.isGeneratingDailyPlan,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            if (uiState.isGeneratingDailyPlan) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Re-Calibrate", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = CyberCardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "AI NEURAL VERDICT:",
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = uiState.dailyPlan?.aiVerdict ?: "Synthesizing optimal task sequence for maximum productivity...",
                        color = TextCyberWhite,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Estimated Flow: ${uiState.dailyPlan?.focusHoursEstimated ?: 0.0} hrs",
                            color = NeonViolet,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Recovery Breaks: ACTIVE",
                            color = NeonGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Chronological Flow Sequence",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        val blocks = uiState.dailyPlan?.scheduleBlocks ?: emptyList()

        if (blocks.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("Calibrating schedule matrix...", color = TextCyberMuted)
                    }
                }
            }
        } else {
            itemsIndexed(blocks) { index, block ->
                FuturisticTimelineCard(
                    block = block,
                    isLast = index == blocks.size - 1,
                    onStartFocus = { onNavigateToFocus(block.taskTitle) }
                )
            }
        }
    }
}

@Composable
fun FuturisticTimelineCard(
    block: AIScheduleBlock,
    isLast: Boolean,
    onStartFocus: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !block.isBreak) { onStartFocus() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (block.isBreak) CyberCardElevated else AccentCoral,
                modifier = Modifier
                    .size(26.dp)
                    .border(1.dp, if (block.isBreak) NeonGreen else NeonCyan, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (block.isBreak) Icons.Default.Coffee else Icons.Default.Schedule,
                        contentDescription = null,
                        tint = if (block.isBreak) NeonGreen else Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(64.dp)
                        .background(CyberCardBorder)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (block.isBreak) CyberCardElevated.copy(alpha = 0.6f) else CyberCardSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (block.isBreak) NeonGreen.copy(alpha = 0.4f) else CyberCardBorder,
                    RoundedCornerShape(16.dp)
                )
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = block.timeSlot,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (block.isBreak) NeonGreen else AccentCoral,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = block.taskTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextCyberWhite,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${block.durationMinutes} mins • ${block.taskCategory}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextCyberMuted)
                    )
                }

                if (!block.isBreak) {
                    IconButton(onClick = onStartFocus) {
                        Surface(
                            shape = CircleShape,
                            color = CyberCardElevated,
                            modifier = Modifier
                                .size(38.dp)
                                .border(1.dp, AccentCoral, CircleShape)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Engage Focus",
                                    tint = AccentCoral,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}