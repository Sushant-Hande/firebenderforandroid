package com.example.firebenderforandroid.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.firebenderforandroid.model.QuizResultEntity

@Database(entities = [QuizResultEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizResultDao(): QuizResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "quiz_db"
            ).build().also { INSTANCE = it }
        }
    }
}
