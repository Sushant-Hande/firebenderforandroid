package com.example.firebenderforandroid.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.firebenderforandroid.model.Quiz
import com.example.firebenderforandroid.repository.QuizRepository

class QuizListViewModel : ViewModel() {
    private val repo = QuizRepository()
    var quizList by mutableStateOf<List<Quiz>>(emptyList())
        private set

    init {
        loadQuizzes()
    }

    private fun loadQuizzes() {
        quizList = repo.getAllQuizzes()
    }
}
