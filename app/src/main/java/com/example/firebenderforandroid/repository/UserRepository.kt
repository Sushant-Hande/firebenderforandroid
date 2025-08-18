package com.example.firebenderforandroid.repository

import android.content.Context
import com.example.firebenderforandroid.model.UserStatsEntity
import com.example.firebenderforandroid.model.QuizResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object UserRepository {
    private lateinit var db: AppDatabase
    private lateinit var username: String

    fun init(context: Context, username: String) {
        db = AppDatabase.getInstance(context)
        this.username = username
    }

    fun getUserStats(): Flow<UserStatsEntity> = flow {
        val userResults = db.quizResultDao().getResultsForUser(username)
        val best = db.quizResultDao().getBestScore(username) ?: 0
        val avg = db.quizResultDao().getAvgScore(username) ?: 0.0
        emit(UserStatsEntity(username, userResults.size, best, avg))
    }

    suspend fun submitQuiz(score: Int, quizId: String) {
        db.quizResultDao().insertQuizResult(
            QuizResultEntity(
                username = username,
                score = score,
                timestamp = System.currentTimeMillis(),
                quizId = quizId
            )
        )
    }
}
