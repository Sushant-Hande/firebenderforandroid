package com.example.firebenderforandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebenderforandroid.model.QuizResultEntity
import com.example.firebenderforandroid.repository.AppDatabase
import com.example.firebenderforandroid.viewmodel.DashboardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DashboardScreen(navController: NavController? = null) {
    val viewModel = remember { DashboardViewModel() }
    val statsState = viewModel.stats.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        var selectedTab = remember { mutableStateOf(0) }
        val tabs = listOf("Dashboard", "Quizzes", "Leaderboard", "History")
        val icons = listOf(
            Icons.Filled.Home,
            Icons.Filled.List,
            Icons.Filled.Leaderboard,
            Icons.Filled.History
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab.value) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "S",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Welcome, User!", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(18.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(18.dp)
                                ) {
                                    val stats = statsState.value
                                    if (stats == null) {
                                        Text(
                                            "Loading stats...",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    } else {
                                        Text(
                                            "Quizzes taken: ${stats.quizzesTaken}",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            "Best score: ${stats.bestScore}",
                                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                                        )
                                        Text(
                                            "Average score: ${"%.1f".format(stats.avgScore)}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            val stats = statsState.value
                            Text(
                                when {
                                    stats == null -> "Please wait..."
                                    stats.bestScore > 8 -> "🔥 You’re on fire!"
                                    stats.bestScore > 0 -> "Keep climbing the leaderboard!"
                                    else -> "Start your first quiz to earn a score!"
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { selectedTab.value = 1 },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Take a Quiz")
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { navController?.navigate("leaderboard") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("View Leaderboard")
                            }
                        }
                    }
                    1 -> {
                        if (navController != null) {
                            QuizListScreen(navController)
                        } else {
                            Text("Something went wrong: navController unavailable.")
                        }
                    }

                    2 -> {
                        LeaderboardTabContent()
                    }

                    3 -> {
                        QuizHistoryTabContent()
                    }
                }
            }
            NavigationBar {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedTab.value == index,
                        onClick = { selectedTab.value = index },
                        icon = { Icon(icons[index], contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardTabContent() {
    val dashboardViewModel = remember { DashboardViewModel() }
    val statsState = dashboardViewModel.stats.collectAsState()
    val yourBestScore = statsState.value?.bestScore ?: 0
    val leaderboard = listOf(
        "Alice" to 12,
        "Bob" to 10,
        "Carol" to 8,
        "You" to yourBestScore
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Leaderboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        leaderboard.forEachIndexed { idx, pair ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "#${idx + 1}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.width(36.dp)
                    )
                    val isCurrentUser = pair.first == "You"
                    Text(
                        pair.first,
                        style = if (isCurrentUser) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        pair.second.toString(),
                        style = if (isCurrentUser) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun QuizHistoryTabContent() {
    val context = LocalContext.current
    val historyState = remember { mutableStateOf<List<QuizResultEntity>?>(null) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getInstance(context)
        val username = "User" // Get from your login/profile in future
        withContext(Dispatchers.IO) {
            historyState.value = db.quizResultDao().getResultsForUser(username)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("My Quiz History", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        if (historyState.value == null) {
            Text("Loading quizzes...")
        } else if (historyState.value?.isEmpty() == true) {
            Text("No quiz history yet.")
        } else {
            historyState.value?.forEach { result ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Quiz: ${result.quizId}",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Score: ${result.score}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            android.text.format.DateFormat.format(
                                "MMM dd, yyyy HH:mm",
                                result.timestamp
                            ).toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
