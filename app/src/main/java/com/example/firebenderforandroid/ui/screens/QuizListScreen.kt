package com.example.firebenderforandroid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebenderforandroid.viewmodel.QuizListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizListScreen(navController: NavController) {
    val viewModel = remember { QuizListViewModel() }
    val quizzes = viewModel.quizList
    val loading = quizzes.isEmpty()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            Text("Select a Quiz", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(quizzes) { quiz ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { navController.navigate("quiz_question?quizId=${quiz.id}") },
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(quiz.title, style = MaterialTheme.typography.titleLarge)
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        quiz.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        "Questions: ${quiz.questions.size}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                AssistChip(
                                    onClick = {},
                                    label = { Text("Easy") },
                                    modifier = Modifier.padding(start = 8.dp),
                                    colors = androidx.compose.material3.AssistChipDefaults.assistChipColors(
                                        labelColor = Color.White,
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                }
                if (quizzes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No quizzes yet!", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
