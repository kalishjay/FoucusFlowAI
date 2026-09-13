package com.focusflow.ai.ui.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focusflow.ai.data.local.AppDatabase
import com.focusflow.ai.data.local.UserDao
import com.focusflow.ai.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val activeUser: UserEntity? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()

    private val _authState = MutableStateFlow(AuthUiState(isLoading = true))
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            userDao.getActiveUserFlow().collect { user ->
                _authState.update {
                    it.copy(
                        isLoggedIn = user != null,
                        activeUser = user,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun login(email: String, pass: String, onResult: (Boolean) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.update { it.copy(errorMessage = "Please enter both email and password") }
            onResult(false)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUserByEmail(email.trim().lowercase())
            if (user != null) {
                if (user.passwordHash == pass) {
                    userDao.deactivateAllSessions()
                    userDao.setActiveSession(user.id)
                    withContext(Dispatchers.Main) {
                        _authState.update { it.copy(errorMessage = null) }
                        onResult(true)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _authState.update { it.copy(errorMessage = "Incorrect password. Please try again.") }
                        onResult(false)
                    }
                }
            } else {
                // If it is a new user logging in directly
                val formattedName = if (email.contains("@")) {
                    email.substringBefore("@").replace(".", " ")
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                } else "Flow Operator"

                val newUserId = userDao.insertUser(
                    UserEntity(
                        email = email.trim().lowercase(),
                        passwordHash = pass,
                        name = formattedName,
                        studentId = "",
                        dailyTaskGoal = 5,
                        streakDays = 0,
                        totalFocusMinutes = 0,
                        productivityLevel = "Novice Focus (Lvl 1)",
                        isActiveSession = true
                    )
                )
                userDao.deactivateAllSessions()
                userDao.setActiveSession(newUserId)
                withContext(Dispatchers.Main) {
                    _authState.update { it.copy(errorMessage = null) }
                    onResult(true)
                }
            }
        }
    }

    fun signUp(name: String, email: String, pass: String, onResult: (Boolean) -> Unit) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.update { it.copy(errorMessage = "Please enter all required information") }
            onResult(false)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val existing = userDao.getUserByEmail(email.trim().lowercase())
            if (existing != null) {
                withContext(Dispatchers.Main) {
                    _authState.update { it.copy(errorMessage = "An account with this email already exists.") }
                    onResult(false)
                }
            } else {
                val newUserId = userDao.insertUser(
                    UserEntity(
                        name = name.trim(),
                        email = email.trim().lowercase(),
                        passwordHash = pass,
                        studentId = "",
                        dailyTaskGoal = 5,
                        streakDays = 0,
                        totalFocusMinutes = 0,
                        productivityLevel = "Novice Focus (Lvl 1)",
                        isActiveSession = true
                    )
                )
                userDao.deactivateAllSessions()
                userDao.setActiveSession(newUserId)
                withContext(Dispatchers.Main) {
                    _authState.update { it.copy(errorMessage = null) }
                    onResult(true)
                }
            }
        }
    }

    fun loginAsGuest(onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            var guest = userDao.getUserByEmail("demo@focusflow.ai")
            if (guest == null) {
                val id = userDao.insertUser(
                    UserEntity(
                        email = "demo@focusflow.ai",
                        passwordHash = "demo123",
                        name = "FocusFlow Explorer",
                        studentId = "",
                        dailyTaskGoal = 5,
                        streakDays = 3,
                        totalFocusMinutes = 75,
                        productivityLevel = "Active Flow (Lvl 2)",
                        isActiveSession = true
                    )
                )
                guest = userDao.getUserById(id)
            }
            userDao.deactivateAllSessions()
            guest?.let { userDao.setActiveSession(it.id) }
            withContext(Dispatchers.Main) {
                _authState.update { it.copy(errorMessage = null) }
                onResult(true)
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deactivateAllSessions()
            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    fun updateProfile(
        name: String,
        dailyGoal: Int,
        focusMins: Int,
        breakMins: Int,
        notifications: Boolean
    ) {
        val user = _authState.value.activeUser ?: return
        viewModelScope.launch(Dispatchers.IO) {
            userDao.updateProfile(user.id, name, dailyGoal, focusMins, breakMins, notifications)
        }
    }

    fun clearError() {
        _authState.update { it.copy(errorMessage = null) }
    }
}