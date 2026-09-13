package com.focusflow.ai.data.ai

import com.focusflow.ai.data.local.TaskEntity
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

data class AITaskAnalysisResult(
    val estimatedMinutes: Int,
    val suggestedPriority: String,
    val subtasks: List<String>,
    val aiRationale: String
)

data class AIScheduleBlock(
    val timeSlot: String,
    val taskTitle: String,
    val taskCategory: String,
    val isBreak: Boolean = false,
    val durationMinutes: Int = 30
)

data class AIDailyPlanResult(
    val scheduleBlocks: List<AIScheduleBlock>,
    val aiVerdict: String,
    val focusHoursEstimated: Double
)

class GeminiAIService(private var apiKey: String = "") {

    fun updateApiKey(newKey: String) {
        apiKey = newKey
    }

    suspend fun analyzeTask(title: String, description: String): AITaskAnalysisResult = withContext(Dispatchers.IO) {
        if (apiKey.isNotBlank()) {
            try {
                val model = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = apiKey
                )
                val prompt = """
                    You are FocusFlow AI productivity assistant. Analyze this task:
                    Title: "$title"
                    Description: "$description"
                    
                    Respond strictly in valid JSON format without markdown ticks:
                    {
                      "estimatedMinutes": 45,
                      "suggestedPriority": "HIGH", // HIGH, MEDIUM, or LOW
                      "subtasks": ["Step 1", "Step 2", "Step 3"],
                      "aiRationale": "Short 1-sentence tip on why this was planned this way"
                    }
                """.trimIndent()

                val response = model.generateContent(prompt)
                val text = response.text?.replace("```json", "")?.replace("```", "")?.trim()
                if (!text.isNullOrBlank()) {
                    val json = JSONObject(text)
                    val est = json.optInt("estimatedMinutes", 45)
                    val prio = json.optString("suggestedPriority", "MEDIUM")
                    val rat = json.optString("aiRationale", "Optimized by FocusFlow AI for steady flow.")
                    val subtasksArray = json.optJSONArray("subtasks")
                    val subtasksList = mutableListOf<String>()
                    if (subtasksArray != null) {
                        for (i in 0 until subtasksArray.length()) {
                            subtasksList.add(subtasksArray.getString(i))
                        }
                    }
                    return@withContext AITaskAnalysisResult(
                        estimatedMinutes = if (est > 0) est else 45,
                        suggestedPriority = if (prio in listOf("HIGH", "MEDIUM", "LOW")) prio else "MEDIUM",
                        subtasks = if (subtasksList.isNotEmpty()) subtasksList else getDefaultSubtasks(title),
                        aiRationale = rat
                    )
                }
            } catch (e: Exception) {
                // Fallback gracefully
            }
        }
        // Intelligent Offline Engine fallback
        generateOfflineAnalysis(title, description)
    }

    suspend fun generateDailyPlan(tasks: List<TaskEntity>): AIDailyPlanResult = withContext(Dispatchers.IO) {
        if (tasks.isEmpty()) {
            return@withContext AIDailyPlanResult(
                scheduleBlocks = listOf(
                    AIScheduleBlock("09:00 AM - 10:00 AM", "Morning Planning & Review", "Personal", false, 60),
                    AIScheduleBlock("10:00 AM - 10:15 AM", "Mindful Flow Break", "Health", true, 15),
                    AIScheduleBlock("10:15 AM - 12:00 PM", "Add tasks to unleash AI Scheduling", "Work", false, 105)
                ),
                aiVerdict = "Add your tasks for today, and FocusFlow AI will optimize your time blocks perfectly.",
                focusHoursEstimated = 2.5
            )
        }

        if (apiKey.isNotBlank()) {
            try {
                val model = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = apiKey
                )
                val tasksDescription = tasks.joinToString(separator = "\n") {
                    "- ${it.title} (${it.category}, Priority: ${it.priority}, Est: ${it.estimatedMinutes} min)"
                }
                val prompt = """
                    You are FocusFlow AI. Create an optimal daily schedule starting at 09:00 AM for these tasks:
                    $tasksDescription

                    Interleave short 15-minute breaks after intense work sessions.
                    Respond strictly in valid JSON format without markdown code blocks:
                    {
                      "aiVerdict": "Focus on high impact academic milestones first before context switching.",
                      "focusHoursEstimated": 4.5,
                      "blocks": [
                        { "timeSlot": "09:00 AM - 10:00 AM", "taskTitle": "Task Name", "taskCategory": "Academic", "isBreak": false, "durationMinutes": 60 },
                        { "timeSlot": "10:00 AM - 10:15 AM", "taskTitle": "Re-charge Break", "taskCategory": "Health", "isBreak": true, "durationMinutes": 15 }
                      ]
                    }
                """.trimIndent()

                val response = model.generateContent(prompt)
                val text = response.text?.replace("```json", "")?.replace("```", "")?.trim()
                if (!text.isNullOrBlank()) {
                    val json = JSONObject(text)
                    val verdict = json.optString("aiVerdict", "Your day is calibrated for peak cognitive efficiency.")
                    val hours = json.optDouble("focusHoursEstimated", 4.0)
                    val blocksArray = json.optJSONArray("blocks")
                    val blocks = mutableListOf<AIScheduleBlock>()
                    if (blocksArray != null) {
                        for (i in 0 until blocksArray.length()) {
                            val b = blocksArray.getJSONObject(i)
                            blocks.add(
                                AIScheduleBlock(
                                    timeSlot = b.optString("timeSlot", "10:00 AM"),
                                    taskTitle = b.optString("taskTitle", "Task"),
                                    taskCategory = b.optString("taskCategory", "Work"),
                                    isBreak = b.optBoolean("isBreak", false),
                                    durationMinutes = b.optInt("durationMinutes", 30)
                                )
                            )
                        }
                    }
                    if (blocks.isNotEmpty()) {
                        return@withContext AIDailyPlanResult(blocks, verdict, hours)
                    }
                }
            } catch (e: Exception) {
                // Fall through to offline engine
            }
        }
        generateOfflineSchedule(tasks)
    }

    private fun generateOfflineAnalysis(title: String, description: String): AITaskAnalysisResult {
        val lower = (title + " " + description).lowercase()
        val estimatedMinutes = when {
            lower.contains("exam") || lower.contains("assignment") || lower.contains("project") || lower.contains("presentation") -> 90
            lower.contains("study") || lower.contains("research") || lower.contains("develop") || lower.contains("code") -> 60
            lower.contains("email") || lower.contains("call") || lower.contains("review") -> 25
            lower.contains("workout") || lower.contains("gym") || lower.contains("walk") -> 45
            else -> 40
        }

        val suggestedPriority = when {
            lower.contains("urgent") || lower.contains("deadline") || lower.contains("exam") || lower.contains("assignment") -> "HIGH"
            lower.contains("important") || lower.contains("project") || lower.contains("meeting") -> "MEDIUM"
            else -> "LOW"
        }

        val subtasks = getDefaultSubtasks(title)
        val rationale = "AI analyzed deadline urgency & complexity: recommended $suggestedPriority priority and $estimatedMinutes mins."

        return AITaskAnalysisResult(
            estimatedMinutes = estimatedMinutes,
            suggestedPriority = suggestedPriority,
            subtasks = subtasks,
            aiRationale = rationale
        )
    }

    private fun getDefaultSubtasks(title: String): List<String> {
        val lower = title.lowercase()
        return when {
            lower.contains("presentation") || lower.contains("slide") -> listOf(
                "Outline key slide points",
                "Draft visuals & content",
                "Rehearse presentation timing"
            )
            lower.contains("assignment") || lower.contains("report") -> listOf(
                "Review requirements & rubric",
                "Complete main sections & analysis",
                "Proofread & finalize citations"
            )
            lower.contains("code") || lower.contains("develop") || lower.contains("app") -> listOf(
                "Set up architecture & dependencies",
                "Implement core logic & tests",
                "Refactor and verify user flow"
            )
            else -> listOf(
                "Gather required resources",
                "Execute primary work session",
                "Review outcomes & mark complete"
            )
        }
    }

    private fun generateOfflineSchedule(tasks: List<TaskEntity>): AIDailyPlanResult {
        val blocks = mutableListOf<AIScheduleBlock>()
        var startHour = 9
        var startMin = 0

        val sortedTasks = tasks.sortedWith(
            compareBy<TaskEntity> {
                when (it.priority) {
                    "HIGH" -> 1
                    "MEDIUM" -> 2
                    else -> 3
                }
            }.thenByDescending { it.estimatedMinutes }
        )

        for ((index, task) in sortedTasks.take(5).withIndex()) {
            val endMinTotal = startMin + task.estimatedMinutes
            val endHour = startHour + (endMinTotal / 60)
            val finalMin = endMinTotal % 60

            val startPeriod = if (startHour < 12) "AM" else "PM"
            val endPeriod = if (endHour < 12) "AM" else "PM"

            val displayStartH = if (startHour > 12) startHour - 12 else if (startHour == 0) 12 else startHour
            val displayEndH = if (endHour > 12) endHour - 12 else if (endHour == 0) 12 else endHour

            val timeSlot = String.format("%02d:%02d %s - %02d:%02d %s", displayStartH, startMin, startPeriod, displayEndH, finalMin, endPeriod)
            blocks.add(
                AIScheduleBlock(
                    timeSlot = timeSlot,
                    taskTitle = task.title,
                    taskCategory = task.category,
                    isBreak = false,
                    durationMinutes = task.estimatedMinutes
                )
            )

            // Insert 15-min break after 2 tasks or long sessions
            if (index < sortedTasks.take(5).size - 1) {
                val breakStartHour = endHour
                val breakStartMin = finalMin
                val breakEndMinTotal = breakStartMin + 15
                val breakEndHour = breakStartHour + (breakEndMinTotal / 60)
                val finalBreakMin = breakEndMinTotal % 60

                val bStartP = if (breakStartHour < 12) "AM" else "PM"
                val bEndP = if (breakEndHour < 12) "AM" else "PM"
                val bDispH1 = if (breakStartHour > 12) breakStartHour - 12 else if (breakStartHour == 0) 12 else breakStartHour
                val bDispH2 = if (breakEndHour > 12) breakEndHour - 12 else if (breakEndHour == 0) 12 else breakEndHour

                blocks.add(
                    AIScheduleBlock(
                        timeSlot = String.format("%02d:%02d %s - %02d:%02d %s", bDispH1, breakStartMin, bStartP, bDispH2, finalBreakMin, bEndP),
                        taskTitle = "Re-charge & Eye Rest Break",
                        taskCategory = "Health",
                        isBreak = true,
                        durationMinutes = 15
                    )
                )

                startHour = breakEndHour
                startMin = finalBreakMin
            } else {
                startHour = endHour
                startMin = finalMin
            }
        }

        val totalWorkMins = sortedTasks.take(5).sumOf { it.estimatedMinutes }
        val hours = totalWorkMins / 60.0

        return AIDailyPlanResult(
            scheduleBlocks = blocks,
            aiVerdict = "Prioritized ${sortedTasks.count { it.priority == "HIGH" }} high-urgency tasks in prime morning focus blocks with spaced recovery intervals.",
            focusHoursEstimated = String.format("%.1f", hours).toDoubleOrNull() ?: hours
        )
    }
}
