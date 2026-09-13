package com.focusflow.ai.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import com.focusflow.ai.ui.theme.*

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToInsights: () -> Unit = {}
) {
    val authState by authViewModel.authState.collectAsState()
    val user = authState.activeUser

    val name = user?.name ?: "Flow Operator"
    val email = user?.email ?: "operator@focusflow.ai"
    val studentId = user?.studentId ?: ""
    val rank = user?.productivityLevel ?: "Active Flow (Lvl 1)"

    var dailyGoal by remember(user?.dailyTaskGoal) { mutableIntStateOf(user?.dailyTaskGoal ?: 5) }
    var focusMins by remember(user?.defaultFocusMinutes) { mutableIntStateOf(user?.defaultFocusMinutes ?: 25) }
    var breakMins by remember(user?.defaultBreakMinutes) { mutableIntStateOf(user?.defaultBreakMinutes ?: 5) }
    var notifications by remember(user?.notificationsEnabled) { mutableStateOf(user?.notificationsEnabled ?: true) }

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
                text = "Account Profile & Settings",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Text(
                text = "Personal preferences saved securely in local storage",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextCyberMuted)
            )
        }

        // Cyber ID Badge Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CyberDarkSurface.copy(alpha = 0.95f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, CyberCardGlowBorder, RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = CyberCardSurface,
                            modifier = Modifier
                                .size(64.dp)
                                .border(2.dp, CyberNeonGradient, CircleShape)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (name.isNotBlank()) name.take(2).uppercase() else "FF",
                                    color = NeonCyan,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = name,
                                color = TextCyberWhite,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = email,
                                color = TextCyberMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CyberCardElevated
                            ) {
                                Text(
                                    text = if (studentId.isNotBlank()) "ID: $studentId" else "VERIFIED ACCOUNT",
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = CyberCardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = AccentCoral, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = rank,
                                color = TextCyberWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NeonGreen.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "DATABASE: ONLINE",
                                color = NeonGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Productivity Preferences Card (Persisted to Room Database)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Flow Parameters (SQLite Persisted)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextCyberWhite,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Daily Target
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Daily Task Target", fontSize = 13.sp, color = TextCyberMuted)
                        Text("$dailyGoal tasks/day", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 13.sp)
                    }
                    Slider(
                        value = dailyGoal.toFloat(),
                        onValueChange = {
                            dailyGoal = it.toInt()
                            authViewModel.updateProfile(name, dailyGoal, focusMins, breakMins, notifications)
                        },
                        valueRange = 1f..15f,
                        steps = 13,
                        colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Default Focus Duration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Default Focus Duration", fontSize = 13.sp, color = TextCyberMuted)
                        Text("$focusMins mins", fontWeight = FontWeight.Bold, color = NeonViolet, fontSize = 13.sp)
                    }
                    Slider(
                        value = focusMins.toFloat(),
                        onValueChange = {
                            focusMins = it.toInt()
                            authViewModel.updateProfile(name, dailyGoal, focusMins, breakMins, notifications)
                        },
                        valueRange = 15f..60f,
                        steps = 8,
                        colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Smart Notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Smart Flow Alerts", fontSize = 13.sp, color = TextCyberWhite)
                            Text("Realtime deadline and recovery triggers", fontSize = 11.sp, color = TextCyberSubtle)
                        }
                        Switch(
                            checked = notifications,
                            onCheckedChange = {
                                notifications = it
                                authViewModel.updateProfile(name, dailyGoal, focusMins, breakMins, notifications)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentCoral
                            )
                        )
                    }
                }
            }
        }

        // Productivity Insights Action Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CyberCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    .clickable { onNavigateToInsights() }
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
                            color = CyberCardElevated,
                            modifier = Modifier
                                .size(42.dp)
                                .border(1.dp, NeonCyan.copy(alpha = 0.7f), CircleShape)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Productivity Telemetry", color = TextCyberWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Flow analytics, Gemini AI key & streak", color = TextCyberMuted, fontSize = 12.sp)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonCyan)
                }
            }
        }

        // Local Data & Offline Storage Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FlowIndigoDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonViolet.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storage, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Local Storage & Data Privacy",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextCyberWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• All tasks and focus metrics are stored securely on this device", fontSize = 12.sp, color = TextCyberMuted)
                    Text("• 100% Offline Capable: Instant sync with zero internet latency", fontSize = 12.sp, color = TextCyberMuted)
                    Text("• Database Engine: High-performance SQLite via Android Room", fontSize = 12.sp, color = NeonGreen)
                }
            }
        }

        // Logout Button
        item {
            Button(
                onClick = {
                    authViewModel.logout {
                        onLogout()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCardElevated),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, NeonRed.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = NeonRed)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out", color = NeonRed, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}