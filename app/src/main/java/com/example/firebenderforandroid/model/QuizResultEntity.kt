package com.example.firebenderforandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val score: Int,
    val timestamp: Long,
    val quizId: String
)
