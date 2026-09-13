package com.focusflow.ai.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.focusflow.ai.data.local.TaskEntity
import com.focusflow.ai.ui.screens.auth.AuthViewModel
import com.focusflow.ai.ui.screens.tasks.TaskViewModel
import com.focusflow.ai.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    taskViewModel: TaskViewModel,
    authViewModel: AuthViewModel,
    onNavigateToTasks: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    onNavigateToFocus: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToInsights: () -> Unit = {}
) {
    val pendingTasks by taskViewModel.pendingTasks.collectAsState()
    val completedTasks by taskViewModel.completedTasks.collectAsState()
    val totalFocusMins by taskViewModel.totalFocusMinutes.collectAsState()
    val uiState by taskViewModel.uiState.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    val user = authState.activeUser
    val userName = user?.name ?: "Operator"
    val dailyGoal = user?.dailyTaskGoal ?: 5
    val streak = user?.streakDays ?: 0

    val totalCount = pendingTasks.size + completedTasks.size
    val progress = if (totalCount > 0) completedTasks.size.toFloat() / totalCount else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000), label = "progress")

    val currentDateStr = remember {
        SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuralCoreGradient)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(14.dp))
            // Futuristic Holographic Dashboard Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hello, ${userName.split(" ").firstOrNull() ?: "Operator"}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = TextCyberWhite,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NeonGreen)
                        )
                    }
                    Text(
                        text = currentDateStr.uppercase(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                // Glowing Holographic User Avatar
                Surface(
                    shape = CircleShape,
                    color = CyberCardSurface,
                    modifier = Modifier
                        .size(48.dp)
                        .border(1.5.dp, CyberNeonGradient, CircleShape)
                        .clickable { onNavigateToProfile() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (userName.isNotBlank()) userName.take(2).uppercase() else "FF",
                            color = NeonCyan,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Futuristic Metrics Strip (Database backed: Streak, Daily Goal, Focus Mins)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak Tile
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CyberCardSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, NeonViolet.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AccentCoral.copy(alpha = 0.15f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = AccentCoral, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("STREAK", fontSize = 9.sp, color = TextCyberSubtle, fontWeight = FontWeight.Bold)
                            Text("$streak Days", fontWeight = FontWeight.Bold, color = TextCyberWhite, fontSize = 13.sp)
                        }
                    }
                }

                // Daily Goal Tile
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CyberCardSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = NeonGreen.copy(alpha = 0.15f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("GOAL", fontSize = 9.sp, color = TextCyberSubtle, fontWeight = FontWeight.Bold)
                            Text("${completedTasks.size}/$dailyGoal", fontWeight = FontWeight.Bold, color = TextCyberWhite, fontSize = 13.sp)
                        }
                    }
                }

                // Focus Minutes Tile
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CyberCardSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = NeonCyan.copy(alpha = 0.15f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("FOCUS", fontSize = 9.sp, color = TextCyberSubtle, fontWeight = FontWeight.Bold)
                            Text("${totalFocusMins ?: 0}m", fontWeight = FontWeight.Bold, color = TextCyberWhite, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Daily Flow Matrix Circular Arc Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CyberDarkSurface.copy(alpha = 0.95f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, CyberCardGlowBorder, RoundedCornerShape(24.dp))
                    .clickable { onNavigateToInsights() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Flow Matrix",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextCyberWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NeonViolet.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = "SYNCHRONIZED",
                                    color = NeonCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${completedTasks.size} of $totalCount tasks completed today",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextCyberMuted)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CyberCardElevated,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "Target: $dailyGoal tasks",
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(86.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            color = CyberCardElevated,
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Round
                        )
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxSize(),
                            color = AccentCoral,
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextCyberWhite
                            )
                        )
                    }
                }
            }
        }

        // Futuristic AI Verdict Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FlowIndigoDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonViolet.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .clickable { onNavigateToPlanner() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(CyberCardElevated)
                            .border(1.dp, NeonCyan.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "FocusFlow AI Verdict",
                                color = TextCyberWhite,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AccentCoral
                            ) {
                                Text(
                                    text = "AI OPTIMIZED",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.dailyPlan?.aiVerdict
                                ?: "Prioritize high-impact deep work during your peak energy hours. Tap here to generate your personalized AI schedule.",
                            color = TextCyberMuted,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp)
                        )
                    }
                }
            }
        }

        // Quick Launch Focus Mode
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, AccentCoral.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    .clickable { onNavigateToFocus(pendingTasks.firstOrNull()?.title ?: "Deep Focus Protocol") }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = AccentCoral,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Quick Focus Protocol",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextCyberWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "25m Deep Work • 5m Recovery",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextCyberMuted)
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigateToFocus(pendingTasks.firstOrNull()?.title ?: "Deep Focus Protocol") },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "ENGAGE", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                }
            }
        }

        // Priority Tasks Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Priority Tasks Today",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextCyberWhite,
                        fontWeight = FontWeight.Bold
                    )
                )
                TextButton(onClick = onNavigateToTasks) {
                    Text(text = "View All (${pendingTasks.size})", color = NeonCyan, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (pendingTasks.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Flow Matrix Clear 🎉",
                            style = MaterialTheme.typography.titleMedium.copy(color = TextCyberWhite)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "All tasks completed. Add new targets or review insights.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextCyberMuted)
                        )
                    }
                }
            }
        } else {
            items(pendingTasks.take(4)) { task ->
                FuturisticTaskItem(
                    task = task,
                    onToggle = { taskViewModel.toggleTaskCompletion(task) },
                    onStartFocus = { onNavigateToFocus(task.title) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(85.dp))
        }
    }
}

@Composable
fun FuturisticTaskItem(
    task: TaskEntity,
    onToggle: () -> Unit,
    onStartFocus: () -> Unit
) {
    val priorityColor = when (task.priority) {
        "HIGH" -> PriorityHigh
        "MEDIUM" -> PriorityMedium
        else -> PriorityLow
    }

    val priorityBg = when (task.priority) {
        "HIGH" -> PriorityHighContainer
        "MEDIUM" -> PriorityMediumContainer
        else -> PriorityLowContainer
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle Complete",
                    tint = if (task.isCompleted) NeonGreen else TextCyberSubtle
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextCyberWhite,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = priorityBg
                    ) {
                        Text(
                            text = task.priority,
                            color = priorityColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${task.estimatedMinutes}m",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextCyberMuted)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${task.category}",
                        style = MaterialTheme.typography.bodySmall.copy(color = NeonCyan)
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = CyberCardElevated,
                modifier = Modifier
                    .size(34.dp)
                    .border(1.dp, NeonViolet.copy(alpha = 0.6f), CircleShape)
                    .clickable { onStartFocus() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Focus",
                        tint = NeonViolet,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}