package com.example.firebenderforandroid.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.firebenderforandroid.model.Question
import com.example.firebenderforandroid.model.Quiz
import com.example.firebenderforandroid.model.QuizQuestionReview
import com.example.firebenderforandroid.repository.QuizRepository

class QuizViewModel(quizId: String?) : ViewModel() {
    private val repository = QuizRepository()
    var quiz: Quiz? by mutableStateOf(null)
    var currentIndex by mutableStateOf(0)
        private set
    var selectedAnswer by mutableStateOf<Int?>(null)
        private set
    var score by mutableStateOf(0)
        private set
    var isQuizComplete by mutableStateOf(false)
        private set

    // Track user's selected answers per question
    private val userAnswers = mutableListOf<Int?>()

    init {
        quiz = quizId?.let { repository.getQuizById(it) }
        // Initialize userAnswers with null entries for each question
        quiz?.questions?.forEach { _ -> userAnswers.add(null) }
    }

    val currentQuestion: Question?
        get() = quiz?.questions?.getOrNull(currentIndex)

    fun submitAnswer(answerIndex: Int) {
        selectedAnswer = answerIndex
        if (currentIndex < userAnswers.size) userAnswers[currentIndex] = answerIndex
    }

    fun commitAnswer() {
        if (selectedAnswer == currentQuestion?.correctAnswerIndex) {
            score++
        }
    }

    fun goToNextQuestion() {
        commitAnswer()
        selectedAnswer = null
        if (currentIndex < (quiz?.questions?.size ?: 0) - 1) {
            currentIndex++
        } else {
            isQuizComplete = true
        }
    }

    fun resetQuiz() {
        currentIndex = 0
        selectedAnswer = null
        score = 0
        isQuizComplete = false
        userAnswers.clear()
        quiz?.questions?.forEach { _ -> userAnswers.add(null) }
    }

    val lastScore: Int
        get() = score

    fun getQuizReview(): List<QuizQuestionReview> {
        val questions = quiz?.questions ?: return emptyList()
        return questions.mapIndexed { idx, q ->
            QuizQuestionReview(
                question = q.text,
                options = q.options,
                correctAnswer = q.options[q.correctAnswerIndex],
                userAnswer = userAnswers.getOrNull(idx)?.let { q.options[it] }
            )
        }
    }
}
