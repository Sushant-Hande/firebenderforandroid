package com.example.firebenderforandroid.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firebenderforandroid.repository.QuizRepository
import com.example.firebenderforandroid.viewmodel.QuizViewModel
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedScreen(content: @Composable () -> Unit) {
    // Pick a random transition each time for fun effect
    val transitionSpec = remember {
        val effects = listOf<ContentTransform>(
            fadeIn() + slideInHorizontally { width -> width } with fadeOut() + slideOutHorizontally { width -> -width },
            fadeIn() with fadeOut(),
            scaleIn() with scaleOut(),
            fadeIn() + scaleIn() with fadeOut() + scaleOut(),
            slideInHorizontally { it / 2 } with slideOutHorizontally { -it / 2 }
        )
        effects[Random.nextInt(effects.size)]
    }
    AnimatedContent(
        targetState = true,
        transitionSpec = { transitionSpec }
    ) { _ ->
        content()
    }
}

@Composable
fun QuizAppNavGraph(navController: NavHostController = rememberNavController()) {
    val quizViewModel = remember { QuizViewModel(null) }

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("splash") { AnimatedScreen { SplashScreen(navController) } }
        composable("dashboard") { AnimatedScreen { DashboardScreen(navController) } }
        composable(
            route = "quiz_question?quizId={quizId}",
            arguments = listOf(
                navArgument("quizId") { nullable = true }
            )
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId")
            if (quizViewModel.quiz == null && quizId != null) {
                quizViewModel.quiz = QuizRepository().getQuizById(quizId)
                quizViewModel.resetQuiz() // ensure all question state is reset
            }
            AnimatedScreen { QuizQuestionScreen(quizId, navController, quizViewModel) }
        }
        composable(
            route = "quiz_result?quizId={quizId}&score={score}",
            arguments = listOf(
                navArgument("quizId") { nullable = true },
                navArgument("score") {
                    type = androidx.navigation.NavType.IntType
                }
            )
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId")
            val score = backStackEntry.arguments?.getInt("score")
            AnimatedScreen { QuizResultScreen(navController, quizId, score, quizViewModel) }
        }
        composable("leaderboard") { AnimatedScreen { LeaderboardScreen(navController) } }
    }
}
