package com.focusflow.ai.ui.screens.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerState(
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val isBreak: Boolean = false,
    val sessionsCompleted: Int = 0,
    val selectedTaskTitle: String = "Deep Work Session"
)

class FocusTimerViewModel : ViewModel() {
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    fun selectTask(title: String) {
        _timerState.update { it.copy(selectedTaskTitle = title) }
    }

    fun toggleStartPause() {
        if (_timerState.value.isRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _timerState.update { it.copy(isRunning = true) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                delay(1000L)
                _timerState.update { it.copy(remainingSeconds = it.remainingSeconds - 1) }
            }
            if (_timerState.value.remainingSeconds == 0) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        _timerState.update { it.copy(isRunning = false) }
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        val defaultSecs = if (_timerState.value.isBreak) 5 * 60 else 25 * 60
        _timerState.update {
            it.copy(
                totalSeconds = defaultSecs,
                remainingSeconds = defaultSecs,
                isRunning = false
            )
        }
    }

    fun setTimerMode(isBreakMode: Boolean) {
        pauseTimer()
        val duration = if (isBreakMode) 5 * 60 else 25 * 60
        _timerState.update {
            it.copy(
                isBreak = isBreakMode,
                totalSeconds = duration,
                remainingSeconds = duration
            )
        }
    }

    private fun onTimerFinished() {
        pauseTimer()
        val wasBreak = _timerState.value.isBreak
        if (!wasBreak) {
            _timerState.update {
                it.copy(
                    sessionsCompleted = it.sessionsCompleted + 1,
                    isBreak = true,
                    totalSeconds = 5 * 60,
                    remainingSeconds = 5 * 60
                )
            }
        } else {
            _timerState.update {
                it.copy(
                    isBreak = false,
                    totalSeconds = 25 * 60,
                    remainingSeconds = 25 * 60
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
