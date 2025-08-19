package com.example.firebenderforandroid.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.LottieConstants
import kotlinx.coroutines.delay
import com.example.firebenderforandroid.viewmodel.QuizViewModel
import com.example.firebenderforandroid.model.QuizQuestionReview

@Composable
fun QuizResultScreen(
    navController: NavController? = null,
    quizId: String? = null,
    score: Int? = null,
    quizViewModel: QuizViewModel
) {
    // In a real app: pass score, user, and quizId via nav arguments/shared ViewModel
    val finalScore = score ?: 0
    var showGreeting by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(250)
        showGreeting = true
    }
    val feedback = when {
        finalScore == 0 -> "😢 Try Again!"
        finalScore > 8 -> "🏅 Excellent!"
        finalScore > 5 -> "👍 Good Job!"
        finalScore > 2 -> "🙂 Almost There!"
        else -> "Keep trying!"
    }
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        val reviewList = quizViewModel.getQuizReview()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = showGreeting,
                enter = androidx.compose.animation.fadeIn(tween(800)),
                exit = androidx.compose.animation.fadeOut(tween(1200))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon: ImageVector = when {
                        finalScore == 0 -> Icons.Filled.Close
                        finalScore > 8 -> Icons.Filled.EmojiEvents
                        finalScore > 5 -> Icons.Filled.ThumbUp
                        else -> Icons.Filled.ThumbUp
                    }
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                    Text(feedback, style = MaterialTheme.typography.headlineLarge)
                }
                if (finalScore > 8) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("confetti.json"))
                    val progress by animateLottieCompositionAsState(
                        composition,
                        iterations = LottieConstants.IterateForever
                    )
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("Your score: $finalScore", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    navController?.popBackStack("dashboard", inclusive = false)
                },
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Text("Back to Quiz List")
            }
            Button(
                onClick = { quizId?.let { navController?.navigate("quiz_question?quizId=$it") } },
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Text("Retake Quiz")
            }
            Button(
                onClick = {
                    val message = "I scored $finalScore on Firebender Quiz!"
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, message)
                    try {
                        context.startActivity(Intent.createChooser(intent, "Share your score"))
                    } catch (e: Exception) {
                        Toast.makeText(context, "No app found to share", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Icon(Icons.Filled.Share, contentDescription = "Share", Modifier.padding(end = 8.dp))
                Text("Share Result")
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Quiz Review Section
            Text("Quiz Review", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))
            reviewList.forEach { result ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    tonalElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Q: ${result.question}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Your Answer: ", style = MaterialTheme.typography.labelMedium)
                        Text(
                            result.userAnswer ?: "No answer",
                            color = if (result.userAnswer == result.correctAnswer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "Correct Answer: ${result.correctAnswer}",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Button(
                            onClick = {
                                val searchUrl = "https://en.wikipedia.org/wiki/${
                                    result.correctAnswer.replace(
                                        ' ',
                                        '_'
                                    )
                                }"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text("Learn More")
                        }
                    }
                }
            }
        }
    }
}
