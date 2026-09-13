package com.focusflow.ai.ui.screens.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focusflow.ai.data.ai.AIDailyPlanResult
import com.focusflow.ai.data.ai.AITaskAnalysisResult
import com.focusflow.ai.data.ai.GeminiAIService
import com.focusflow.ai.data.local.AppDatabase
import com.focusflow.ai.data.local.TaskEntity
import com.focusflow.ai.data.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TaskUiState(
    val selectedCategory: String = "All",
    val selectedPriority: String = "ALL",
    val isAnalyzingWithAI: Boolean = false,
    val aiAnalysisResult: AITaskAnalysisResult? = null,
    val isGeneratingDailyPlan: Boolean = false,
    val dailyPlan: AIDailyPlanResult? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    private val db = AppDatabase.getDatabase(application)
    private val userDao = db.userDao()

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    // Observe the currently active user's email (switches automatically on login/logout)
    private val activeUserEmail: StateFlow<String> = userDao.getActiveUserFlow()
        .map { user -> user?.email ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    init {
        val aiService = GeminiAIService()
        repository = TaskRepository(db.taskDao(), aiService)
    }

    // Reactive: when active user changes, queries automatically switch to new user's data
    val allTasks: StateFlow<List<TaskEntity>> = activeUserEmail
        .flatMapLatest { email -> if (email.isNotEmpty()) repository.allTasks(email) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingTasks: StateFlow<List<TaskEntity>> = activeUserEmail
        .flatMapLatest { email -> if (email.isNotEmpty()) repository.pendingTasks(email) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedTasks: StateFlow<List<TaskEntity>> = activeUserEmail
        .flatMapLatest { email -> if (email.isNotEmpty()) repository.completedTasks(email) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val focusSessions = activeUserEmail
        .flatMapLatest { email -> if (email.isNotEmpty()) repository.focusSessions(email) else flowOf(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalFocusMinutes = activeUserEmail
        .flatMapLatest { email -> if (email.isNotEmpty()) repository.totalFocusMinutes(email) else flowOf(0) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setCategoryFilter(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setPriorityFilter(priority: String) {
        _uiState.update { it.copy(selectedPriority = priority) }
    }

    fun addTask(
        title: String,
        description: String,
        deadline: String,
        priority: String,
        estimatedMinutes: Int,
        category: String,
        subtasks: List<String>
    ) {
        viewModelScope.launch {
            val userId = activeUserEmail.value
            if (userId.isEmpty()) return@launch // Not logged in — don't create orphan tasks

            val subtasksJson = subtasks.joinToString("|")
            val task = TaskEntity(
                userId = userId,
                title = title,
                description = description,
                deadline = deadline,
                priority = priority,
                estimatedMinutes = estimatedMinutes,
                category = category,
                subtasksJson = subtasksJson,
                totalSubtasksCount = subtasks.size
            )
            repository.insertTask(task)
            clearAIAnalysis()
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun analyzeWithAI(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzingWithAI = true) }
            val result = repository.analyzeTaskWithAI(title, description)
            _uiState.update {
                it.copy(
                    isAnalyzingWithAI = false,
                    aiAnalysisResult = result
                )
            }
        }
    }

    fun clearAIAnalysis() {
        _uiState.update { it.copy(aiAnalysisResult = null, isAnalyzingWithAI = false) }
    }

    fun generateDailyPlan() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingDailyPlan = true) }
            val tasks = pendingTasks.value
            val plan = repository.generateDailyPlanWithAI(tasks)
            _uiState.update {
                it.copy(
                    isGeneratingDailyPlan = false,
                    dailyPlan = plan
                )
            }
        }
    }

    fun logFocusSession(taskId: Long?, taskTitle: String, durationMinutes: Int) {
        viewModelScope.launch {
            val userId = activeUserEmail.value
            if (userId.isEmpty()) return@launch
            repository.logFocusSession(userId, taskId, taskTitle, durationMinutes)
        }
    }

    fun updateApiKey(apiKey: String) {
        repository.updateApiKey(apiKey)
    }
}
