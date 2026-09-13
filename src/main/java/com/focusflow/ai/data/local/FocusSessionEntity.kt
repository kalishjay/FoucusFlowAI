package com.focusflow.ai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String = "", // Links session to logged-in user email
    val taskId: Long? = null,
    val taskTitle: String = "Deep Focus Session",
    val durationMinutes: Int = 25,
    val timestamp: Long = System.currentTimeMillis()
)
