package com.example.firebenderforandroid.model

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
