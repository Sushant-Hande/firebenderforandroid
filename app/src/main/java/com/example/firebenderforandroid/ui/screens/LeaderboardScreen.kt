package com.example.firebenderforandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebenderforandroid.viewmodel.DashboardViewModel

@Composable
fun LeaderboardScreen(navController: NavController? = null) {
    val dashboardViewModel = remember { DashboardViewModel() }
    val stats by dashboardViewModel.stats.collectAsState()
    val yourBestScore = stats?.bestScore ?: 0
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
