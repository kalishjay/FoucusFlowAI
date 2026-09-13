package com.focusflow.ai.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllTasks(userId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = 0 ORDER BY CASE priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 ELSE 4 END, createdAt ASC")
    fun getPendingTasks(userId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE userId = :userId AND isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTasks(userId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Long, isCompleted: Boolean)

    @Query("UPDATE tasks SET scheduledTimeSlot = :timeSlot WHERE id = :taskId")
    suspend fun updateTaskSchedule(taskId: Long, timeSlot: String)

    // Focus Sessions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSessionEntity): Long

    @Query("SELECT * FROM focus_sessions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllFocusSessions(userId: String): Flow<List<FocusSessionEntity>>

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM focus_sessions WHERE userId = :userId")
    fun getTotalFocusMinutes(userId: String): Flow<Int?>
}
