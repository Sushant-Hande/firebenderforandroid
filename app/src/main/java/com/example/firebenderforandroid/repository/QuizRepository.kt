package com.example.firebenderforandroid.repository

import com.example.firebenderforandroid.model.Quiz
import com.example.firebenderforandroid.model.Question

class QuizRepository {
    fun getAllQuizzes(): List<Quiz> {
        // TODO: Replace with real data source
        return listOf(
            Quiz(
                id = "1",
                title = "General Knowledge",
                description = "Test your general knowledge!",
                questions = listOf(
                    Question(
                        id = "1",
                        text = "What is the capital of France?",
                        options = listOf("Paris", "Berlin", "Rome", "Madrid"),
                        correctAnswerIndex = 0
                    ),
                    Question(
                        id = "2",
                        text = "Who wrote 'Romeo & Juliet'?",
                        options = listOf("Shakespeare", "Dickens", "Austen", "Hemingway"),
                        correctAnswerIndex = 0
                    )
                )
            )
        )
    }

    fun getQuizById(id: String): Quiz? {
        return getAllQuizzes().find { it.id == id }
    }
}
