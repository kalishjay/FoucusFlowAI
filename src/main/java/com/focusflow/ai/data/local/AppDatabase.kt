package com.focusflow.ai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserEntity::class, TaskEntity::class, FocusSessionEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focusflow_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-seed demo account into SQLite
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.userDao()?.insertUser(
                                UserEntity(
                                    email = "demo@focusflow.ai",
                                    passwordHash = "demo123",
                                    name = "Demo User",
                                    studentId = "",
                                    dailyTaskGoal = 5,
                                    defaultFocusMinutes = 25,
                                    defaultBreakMinutes = 5,
                                    streakDays = 0,
                                    totalFocusMinutes = 0,
                                    productivityLevel = "Novice Flow (Lvl 1)",
                                    notificationsEnabled = true,
                                    darkModeEnabled = true,
                                    isActiveSession = false // Fresh install starts at Login/Sign-up
                                )
                            )
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}