package com.focusflow.ai.ui.screens.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.focusflow.ai.ui.screens.auth.AuthViewModel
import com.focusflow.ai.ui.screens.tasks.TaskViewModel
import com.focusflow.ai.ui.theme.*

@Composable
fun InsightsScreen(
    taskViewModel: TaskViewModel,
    authViewModel: AuthViewModel? = null
) {
    val authState by authViewModel?.authState?.collectAsState() ?: remember { mutableStateOf(com.focusflow.ai.ui.screens.auth.AuthUiState()) }
    val userStreak = authState.activeUser?.streakDays ?: 0
    val completedTasks by taskViewModel.completedTasks.collectAsState()
    val pendingTasks by taskViewModel.pendingTasks.collectAsState()
    val totalFocusMins by taskViewModel.totalFocusMinutes.collectAsState()
    val focusSessions by taskViewModel.focusSessions.collectAsState()

    var apiKeyInput by remember { mutableStateOf("") }
    var showApiKeySaved by remember { mutableStateOf(false) }

    val totalHours = ((totalFocusMins ?: 0) / 60.0)
    val totalTasks = completedTasks.size + pendingTasks.size
    val completionRate = if (totalTasks > 0) (completedTasks.size.toFloat() / totalTasks * 100).toInt() else 0

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
                text = "Productivity Telemetry",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            )
            Text(
                text = "Realtime cognitive metrics and persistent SQLite analytics",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextCyberMuted)
            )
        }

        // 3-Metric Summary Cards Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FuturisticMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Focus Hours",
                    value = String.format("%.1fh", totalHours),
                    icon = Icons.Default.Timer,
                    iconBg = CyberCardElevated,
                    iconTint = NeonCyan,
                    glowColor = NeonCyan
                )
                FuturisticMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Done Targets",
                    value = "${completedTasks.size}",
                    icon = Icons.Default.CheckCircle,
                    iconBg = CyberCardElevated,
                    iconTint = AccentCoral,
                    glowColor = AccentCoral
                )
                FuturisticMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Flow Rate",
                    value = "$completionRate%",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    iconBg = CyberCardElevated,
                    iconTint = NeonGreen,
                    glowColor = NeonGreen
                )
            }
        }

        // Streak Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FlowIndigoDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, CyberCardGlowBorder, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(CyberCardElevated)
                            .border(1.dp, AccentCoral, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = AccentCoral,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = if (userStreak > 0) "$userStreak-Day Flow Streak! 🔥" else "0-Day Flow Streak • Ready to Launch 🚀",
                            color = TextCyberWhite,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (userStreak > 0) 
                                "Persistent streak maintained across sessions. Cognitive endurance optimal!" 
                            else 
                                "Complete daily targets to establish your flow streak and ignite your cognitive momentum!",
                            color = TextCyberMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Focus Sessions Database History
        item {
            Text(
                text = "Recent Focus Sessions (Room DB)",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        if (focusSessions.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("No completed focus sessions yet. Engage a 25m Pomodoro to log data!", color = TextCyberMuted)
                    }
                }
            }
        } else {
            items(focusSessions.take(5).size) { i ->
                val session = focusSessions[i]
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = session.taskTitle,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextCyberWhite
                                )
                            )
                        }
                        Text(
                            text = "+${session.durationMinutes} mins",
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Gemini AI Engine Key Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google Gemini AI Core",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextCyberWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Built-in intelligent neural engine active. Enter a custom Gemini API key for live cloud inference:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextCyberMuted)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        placeholder = { Text("Paste Google Gemini API Key (Optional)", color = TextCyberSubtle) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextCyberWhite,
                            unfocusedTextColor = TextCyberWhite,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            taskViewModel.updateApiKey(apiKeyInput.trim())
                            showApiKeySaved = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FlowIndigo),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .align(Alignment.End)
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        Text(if (showApiKeySaved) "Connected!" else "Save Key", color = TextCyberWhite)
                    }
                }
            }
        }
    }
}

@Composable
fun FuturisticMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    glowColor: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
        modifier = modifier.border(1.dp, glowColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                shape = CircleShape,
                color = iconBg,
                modifier = Modifier
                    .size(34.dp)
                    .border(1.dp, glowColor, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(color = TextCyberMuted)
            )
        }
    }
}