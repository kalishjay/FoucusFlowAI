package com.focusflow.ai.data.repository

import com.focusflow.ai.data.ai.AIDailyPlanResult
import com.focusflow.ai.data.ai.AITaskAnalysisResult
import com.focusflow.ai.data.ai.GeminiAIService
import com.focusflow.ai.data.local.FocusSessionEntity
import com.focusflow.ai.data.local.TaskDao
import com.focusflow.ai.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val aiService: GeminiAIService
) {
    // All queries now require userId for per-user data isolation
    fun allTasks(userId: String): Flow<List<TaskEntity>> = taskDao.getAllTasks(userId)
    fun pendingTasks(userId: String): Flow<List<TaskEntity>> = taskDao.getPendingTasks(userId)
    fun completedTasks(userId: String): Flow<List<TaskEntity>> = taskDao.getCompletedTasks(userId)
    fun focusSessions(userId: String): Flow<List<FocusSessionEntity>> = taskDao.getAllFocusSessions(userId)
    fun totalFocusMinutes(userId: String): Flow<Int?> = taskDao.getTotalFocusMinutes(userId)

    suspend fun insertTask(task: TaskEntity): Long {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun toggleTaskCompletion(task: TaskEntity) {
        taskDao.updateTaskCompletion(task.id, !task.isCompleted)
    }

    suspend fun logFocusSession(userId: String, taskId: Long?, taskTitle: String, durationMinutes: Int) {
        taskDao.insertFocusSession(
            FocusSessionEntity(
                userId = userId,
                taskId = taskId,
                taskTitle = taskTitle,
                durationMinutes = durationMinutes
            )
        )
    }

    suspend fun analyzeTaskWithAI(title: String, description: String): AITaskAnalysisResult {
        return aiService.analyzeTask(title, description)
    }

    suspend fun generateDailyPlanWithAI(tasks: List<TaskEntity>): AIDailyPlanResult {
        return aiService.generateDailyPlan(tasks)
    }

    fun updateApiKey(key: String) {
        aiService.updateApiKey(key)
    }
}
