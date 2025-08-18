package com.example.firebenderforandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavBackStackEntry

@Composable
fun QuizAppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable(
            route = "quiz_question?quizId={quizId}",
            arguments = listOf(
                androidx.navigation.navArgument("quizId") { nullable = true }
            )
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId")
            QuizQuestionScreen(quizId, navController)
        }
        composable(
            route = "quiz_result?quizId={quizId}&score={score}",
            arguments = listOf(
                androidx.navigation.navArgument("quizId") { nullable = true },
                androidx.navigation.navArgument("score") {
                    type = androidx.navigation.NavType.IntType
                }
            )
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId")
            val score = backStackEntry.arguments?.getInt("score")
            QuizResultScreen(navController, quizId = quizId, score = score)
        }
        composable("leaderboard") { LeaderboardScreen(navController) }
    }
}
