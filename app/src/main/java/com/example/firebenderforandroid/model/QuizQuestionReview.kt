package com.example.firebenderforandroid.model

data class QuizQuestionReview(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val userAnswer: String?
)
