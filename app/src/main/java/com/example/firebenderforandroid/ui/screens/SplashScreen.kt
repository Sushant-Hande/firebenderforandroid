package com.example.firebenderforandroid.ui.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController? = null) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
        delay(1800)
        navController?.navigate("quiz_list") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(visible = visible, enter = fadeIn() + scaleIn()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Sample app icon, replace with actual resource name if exists
                    // Image(painterResource(id = R.drawable.ic_firebender), contentDescription = "App Logo", modifier = Modifier.size(96.dp))
                    Text(
                        "🔥",
                        fontSize = MaterialTheme.typography.displayLarge.fontSize,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        "Firebender Quiz",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
