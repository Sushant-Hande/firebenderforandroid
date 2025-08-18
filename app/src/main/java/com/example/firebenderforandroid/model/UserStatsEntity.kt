package com.example.firebenderforandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val username: String,
    val quizzesTaken: Int = 0,
    val bestScore: Int = 0,
    val avgScore: Double = 0.0
)
