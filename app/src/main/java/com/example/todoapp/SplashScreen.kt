package com.example.todoapp

import android.app.Activity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoapp.ads.AdManager
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RoyalBlue
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()
    val adManager = remember { AdManager.getInstance() }
    
    // Animation states
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1500),
        label = "alpha"
    )
    
    // Initialize AdManager
    LaunchedEffect(Unit) {
        adManager.initialize(context)
    }
    
    // Handle splash screen timing and navigation
    LaunchedEffect(Unit) {
        delay(2500) // Show splash for 2.5 seconds
        
        // Show App Open Ad if available, then navigate
        adManager.showAppOpenAd(activity) {
            // Navigate based on authentication status
            if (auth.currentUser != null) {
                navController.navigate("task_list") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        RoyalBlue,
                        DarkBlue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
                    .alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Name
            Text(
                text = "ToDo App",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tagline
            Text(
                text = "Organize your tasks efficiently",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.alpha(alpha)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Loading indicator
            androidx.compose.material3.CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .alpha(alpha)
            )
        }
    }
}