package com.focusflow.ai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String = "", // Links task directly to the logged-in user email
    val title: String,
    val description: String = "",
    val deadline: String = "Today",
    val priority: String = "MEDIUM", // HIGH, MEDIUM, LOW
    val estimatedMinutes: Int = 30,
    val category: String = "Work", // Work, Personal, Academic, Health
    val isCompleted: Boolean = false,
    val subtasksJson: String = "", // JSON or formatted subtasks: "Task 1|Task 2"
    val completedSubtasksCount: Int = 0,
    val totalSubtasksCount: Int = 0,
    val scheduledTimeSlot: String = "", // e.g. "09:00 AM - 10:00 AM"
    val createdAt: Long = System.currentTimeMillis()
)
