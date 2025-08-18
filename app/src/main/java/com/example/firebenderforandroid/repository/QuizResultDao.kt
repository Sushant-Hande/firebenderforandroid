package com.example.firebenderforandroid.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.firebenderforandroid.model.QuizResultEntity

@Dao
interface QuizResultDao {
    @Insert
    suspend fun insertQuizResult(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results WHERE username = :username ORDER BY timestamp DESC")
    suspend fun getResultsForUser(username: String): List<QuizResultEntity>

    @Query("SELECT MAX(score) FROM quiz_results WHERE username = :username")
    suspend fun getBestScore(username: String): Int?

    @Query("SELECT AVG(score) FROM quiz_results WHERE username = :username")
    suspend fun getAvgScore(username: String): Double?
}
