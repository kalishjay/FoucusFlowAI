package com.focusflow.ai.ui.screens.tasks

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.SolidColor
import com.focusflow.ai.data.local.TaskEntity
import com.focusflow.ai.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    taskViewModel: TaskViewModel,
    onNavigateToFocus: (String) -> Unit
) {
    val allTasks by taskViewModel.allTasks.collectAsState()
    val uiState by taskViewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Academic", "Work", "Personal", "Health")
    val priorities = listOf("ALL", "HIGH", "MEDIUM", "LOW")

    val filteredTasks = allTasks.filter { task ->
        val matchesCategory = uiState.selectedCategory == "All" || task.category == uiState.selectedCategory
        val matchesPriority = uiState.selectedPriority == "ALL" || task.priority == uiState.selectedPriority
        matchesCategory && matchesPriority
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = AccentCoral,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .border(1.5.dp, CyberNeonGradient, RoundedCornerShape(20.dp)),
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = {
                    Text(
                        text = "New Target",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeuralCoreGradient)
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Task Matrix",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextCyberWhite,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            )
            Text(
                text = "Organize and optimize your cognitive daily targets",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextCyberMuted)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Weekly Calendar Bar
            val calendarDays = remember {
                val cal = Calendar.getInstance()
                val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val numFormat = SimpleDateFormat("d", Locale.getDefault())
                (0..6).map { offset ->
                    val c = Calendar.getInstance()
                    c.add(Calendar.DAY_OF_YEAR, offset)
                    val isToday = offset == 0
                    val dayName = if (isToday) "Today" else dayFormat.format(c.time)
                    val dayNum = numFormat.format(c.time)
                    Triple(dayName, dayNum, offset)
                }
            }
            var selectedDayIndex by remember { mutableIntStateOf(0) }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(calendarDays) { (dayName, dayNum, idx) ->
                    val isSel = selectedDayIndex == idx
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) NeonViolet else CyberCardSurface,
                        modifier = Modifier
                            .width(58.dp)
                            .border(
                                1.dp,
                                if (isSel) NeonCyan else CyberCardBorder,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedDayIndex = idx }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = dayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.White else TextCyberMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dayNum,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.White else TextCyberWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Streamlined Category Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = uiState.selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { taskViewModel.setCategoryFilter(cat) },
                        label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyberCardElevated,
                            selectedLabelColor = NeonCyan,
                            containerColor = CyberCardSurface,
                            labelColor = TextCyberMuted
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) NeonCyan else CyberCardBorder,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberCardSurface.copy(alpha = 0.85f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(22.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(CyberCardElevated)
                                    .border(1.5.dp, NeonCyan.copy(alpha = 0.6f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TaskAlt,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Task Matrix Clear",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextCyberWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Zero targets in this category. Tap below to initialize a task with AI breakdown.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextCyberMuted,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentCoral),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Initialize Target", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 85.dp)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        FuturisticTaskCard(
                            task = task,
                            onToggle = { taskViewModel.toggleTaskCompletion(task) },
                            onDelete = { taskViewModel.deleteTask(task) },
                            onStartFocus = { onNavigateToFocus(task.title) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            taskViewModel = taskViewModel,
            onDismiss = {
                showAddDialog = false
                taskViewModel.clearAIAnalysis()
            }
        )
    }
}

@Composable
fun FuturisticTaskCard(
    task: TaskEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onStartFocus: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

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

    val subtasks = remember(task.subtasksJson) {
        if (task.subtasksJson.isNotBlank()) task.subtasksJson.split("|") else emptyList()
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) CyberCardSurface.copy(alpha = 0.6f) else CyberCardSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (task.isCompleted) SolidColor(CyberCardBorder) else CyberCardGlowBorder,
                RoundedCornerShape(18.dp)
            )
            .clickable { if (subtasks.isNotEmpty() || task.description.isNotBlank()) isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggle, modifier = Modifier.size(30.dp)) {
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
                            color = if (task.isCompleted) TextCyberSubtle else TextCyberWhite,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(6.dp), color = priorityBg) {
                            Text(
                                text = task.priority,
                                color = priorityColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${task.category}",
                            style = MaterialTheme.typography.bodySmall.copy(color = NeonCyan)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${task.estimatedMinutes}m",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextCyberMuted)
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = CyberCardElevated,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, NeonViolet.copy(alpha = 0.6f), CircleShape)
                        .clickable { onStartFocus() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Focus",
                            tint = NeonViolet,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = TextCyberSubtle,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp, start = 40.dp)) {
                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextCyberMuted)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (subtasks.isNotEmpty()) {
                        Text(
                            text = "AI Checklist Protocol:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        subtasks.forEach { sub ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = AccentCoral,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = sub,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextCyberWhite)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    taskViewModel: TaskViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Academic") }
    var priority by remember { mutableStateOf("MEDIUM") }
    var estimatedMinutes by remember { mutableIntStateOf(30) }
    var subtasks by remember { mutableStateOf(listOf<String>()) }

    val uiState by taskViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.aiAnalysisResult) {
        uiState.aiAnalysisResult?.let { result ->
            priority = result.suggestedPriority
            estimatedMinutes = result.estimatedMinutes
            subtasks = result.subtasks
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .imePadding()
                .border(1.5.dp, CyberNeonGradient, RoundedCornerShape(24.dp))
                .padding(vertical = 12.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Initialize Task Target",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextCyberWhite,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Task Objective", color = TextCyberMuted) },
                        placeholder = { Text("e.g. Prepare MADD presentation slides", color = TextCyberSubtle) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextCyberWhite,
                            unfocusedTextColor = TextCyberWhite,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description & Notes (Optional)", color = TextCyberMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextCyberWhite,
                            unfocusedTextColor = TextCyberWhite,
                            focusedBorderColor = NeonViolet,
                            unfocusedBorderColor = CyberCardBorder
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )
                }

                // AI Assist Button
                item {
                    Button(
                        onClick = { taskViewModel.analyzeWithAI(title, description) },
                        enabled = title.isNotBlank() && !uiState.isAnalyzingWithAI,
                        colors = ButtonDefaults.buttonColors(containerColor = FlowIndigo),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    ) {
                        if (uiState.isAnalyzingWithAI) {
                            CircularProgressIndicator(
                                color = NeonCyan,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Neural AI Analyzing...", fontSize = 13.sp, color = NeonCyan)
                        } else {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ask AI to Estimate & Break Down", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextCyberWhite)
                        }
                    }
                }

                if (uiState.aiAnalysisResult != null) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CyberCardElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, NeonViolet.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("AI Recommendation:", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 12.sp)
                                }
                                Text(
                                    text = uiState.aiAnalysisResult!!.aiRationale,
                                    fontSize = 12.sp,
                                    color = TextCyberMuted,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Category selection
                item {
                    Text("Category", style = MaterialTheme.typography.labelMedium.copy(color = TextCyberMuted))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Academic", "Work", "Personal", "Health").forEach { cat ->
                            val isSel = category == cat
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) NeonViolet else CyberCardElevated,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { category = cat }
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSel) Color.White else TextCyberMuted,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Priority Selection
                item {
                    Text("Priority", style = MaterialTheme.typography.labelMedium.copy(color = TextCyberMuted))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("HIGH", "MEDIUM", "LOW").forEach { p ->
                            val isSel = priority == p
                            val col = when (p) {
                                "HIGH" -> PriorityHigh
                                "MEDIUM" -> PriorityMedium
                                else -> PriorityLow
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) col else CyberCardElevated,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { priority = p }
                            ) {
                                Text(
                                    text = p,
                                    color = if (isSel) Color.White else TextCyberMuted,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Estimated Duration Slider
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Estimated Duration", style = MaterialTheme.typography.labelMedium.copy(color = TextCyberMuted))
                        Text("${estimatedMinutes} mins", fontWeight = FontWeight.Bold, color = AccentCoral, fontSize = 13.sp)
                    }
                    Slider(
                        value = estimatedMinutes.toFloat(),
                        onValueChange = { estimatedMinutes = it.toInt() },
                        valueRange = 10f..180f,
                        steps = 16,
                        colors = SliderDefaults.colors(
                            thumbColor = AccentCoral,
                            activeTrackColor = AccentCoral,
                            inactiveTrackColor = CyberCardBorder
                        )
                    )
                }

                // Subtasks preview if any
                if (subtasks.isNotEmpty()) {
                    item {
                        Text("AI Subtask Checklist (${subtasks.size})", style = MaterialTheme.typography.labelMedium.copy(color = NeonCyan))
                        subtasks.forEach { st ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(st, fontSize = 12.sp, color = TextCyberWhite)
                            }
                        }
                    }
                }

                // Deadline Selection
                item {
                    Text("Target Deadline", style = MaterialTheme.typography.labelMedium.copy(color = TextCyberMuted))
                    var selectedDeadline by remember { mutableStateOf("Today") }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Today", "Tomorrow", "This Week").forEach { dl ->
                            val isSel = selectedDeadline == dl
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) NeonCyan.copy(alpha = 0.2f) else CyberCardElevated,
                                modifier = Modifier
                                    .weight(1f)
                                    .border(1.dp, if (isSel) NeonCyan else CyberCardBorder, RoundedCornerShape(10.dp))
                                    .clickable { selectedDeadline = dl }
                            ) {
                                Text(
                                    text = dl,
                                    color = if (isSel) NeonCyan else TextCyberMuted,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel", color = TextCyberMuted, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    taskViewModel.addTask(
                                        title = title.trim(),
                                        description = description.trim(),
                                        deadline = "Today",
                                        priority = priority,
                                        estimatedMinutes = estimatedMinutes,
                                        category = category,
                                        subtasks = subtasks
                                    )
                                    onDismiss()
                                }
                            },
                            enabled = title.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentCoral,
                                disabledContainerColor = CyberCardElevated
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .height(46.dp)
                                .border(
                                    1.dp,
                                    if (title.isNotBlank()) NeonCyan.copy(alpha = 0.6f) else CyberCardBorder,
                                    RoundedCornerShape(14.dp)
                                )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save to Matrix", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}