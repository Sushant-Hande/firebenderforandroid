package com.example.firebenderforandroid.model

import com.example.firebenderforandroid.model.Question

data class Quiz(
    val id: String,
    val title: String,
    val description: String,
    val questions: List<Question>
)
