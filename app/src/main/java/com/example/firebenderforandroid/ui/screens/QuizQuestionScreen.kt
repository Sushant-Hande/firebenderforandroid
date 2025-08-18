package com.example.firebenderforandroid.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebenderforandroid.repository.UserRepository
import com.example.firebenderforandroid.viewmodel.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun QuizQuestionScreen(quizId: String?, navController: NavController) {
    val viewModel = remember { QuizViewModel(quizId) }
    val currentQuestion = viewModel.currentQuestion
    val totalQuestions = viewModel.quiz?.questions?.size ?: 0
    val progress =
        if (totalQuestions != 0) (viewModel.currentIndex + 1) / totalQuestions.toFloat() else 0f

    if (viewModel.isQuizComplete) {
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                UserRepository.submitQuiz(viewModel.score, quizId ?: "")
            }
            navController.navigate("quiz_result?quizId=${quizId ?: ""}&score=${viewModel.score}") {
                popUpTo("quiz_question") { inclusive = true }
            }
        }
    } else {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Question ${(viewModel.currentIndex + 1).coerceAtLeast(1)} of $totalQuestions",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "${viewModel.score} pts",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                AnimatedVisibility(
                    visible = currentQuestion != null,
                    enter = androidx.compose.animation.fadeIn(tween(300))
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                currentQuestion?.text.orEmpty(),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            currentQuestion?.options?.forEachIndexed { idx, option ->
                                val selected = viewModel.selectedAnswer == idx
                                val backgroundColor = if (selected)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                                val textColor = if (selected)
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurface
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(backgroundColor)
                                        .clickable(enabled = true) {
                                            viewModel.submitAnswer(idx)
                                        },
                                    tonalElevation = if (selected) 3.dp else 0.dp,
                                    shadowElevation = if (selected) 8.dp else 0.dp,
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = selected,
                                            onClick = { viewModel.submitAnswer(idx) },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                        Text(
                                            option,
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = textColor
                                        )
                                    }
                                }
                            }
                            if (viewModel.selectedAnswer != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.goToNextQuestion() },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text(if (viewModel.currentIndex == totalQuestions - 1) "Finish" else "Next")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
