package com.focusflow.ai.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isActiveSession = 1 LIMIT 1")
    fun getActiveUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE isActiveSession = 1 LIMIT 1")
    suspend fun getActiveUser(): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isActiveSession = 0")
    suspend fun deactivateAllSessions()

    @Query("UPDATE users SET isActiveSession = 1 WHERE id = :userId")
    suspend fun setActiveSession(userId: Long)

    @Query("UPDATE users SET streakDays = :streak WHERE id = :userId")
    suspend fun updateStreak(userId: Long, streak: Int)

    @Query("UPDATE users SET totalFocusMinutes = totalFocusMinutes + :additionalMinutes WHERE id = :userId")
    suspend fun addFocusMinutes(userId: Long, additionalMinutes: Int)

    @Query("UPDATE users SET name = :name, dailyTaskGoal = :dailyGoal, defaultFocusMinutes = :focusMins, defaultBreakMinutes = :breakMins, notificationsEnabled = :notifications WHERE id = :userId")
    suspend fun updateProfile(
        userId: Long,
        name: String,
        dailyGoal: Int,
        focusMins: Int,
        breakMins: Int,
        notifications: Boolean
    )
}