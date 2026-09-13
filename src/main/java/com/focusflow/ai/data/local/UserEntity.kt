package com.focusflow.ai.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val passwordHash: String,
    val name: String,
    val studentId: String = "IT22550330",
    val dailyTaskGoal: Int = 5,
    val defaultFocusMinutes: Int = 25,
    val defaultBreakMinutes: Int = 5,
    val streakDays: Int = 5,
    val totalFocusMinutes: Int = 120,
    val productivityLevel: String = "Flow Master (Lvl 4)",
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = true,
    val isActiveSession: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)