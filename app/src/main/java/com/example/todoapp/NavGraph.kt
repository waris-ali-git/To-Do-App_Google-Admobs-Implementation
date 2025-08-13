package com.example.todoapp

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.ads.AdManager

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = viewModel()
    val context = LocalContext.current
    val activity = context as Activity
    val adManager = remember { AdManager.getInstance() }
    
    // Track navigation count for interstitial ad
    var navigationCount by remember { mutableIntStateOf(0) }

    NavHost(navController = navController, startDestination = "splash") {
        // Splash Screen (Screen 1)
        composable("splash") {
            SplashScreen(navController = navController)
        }
        
        // Login Screen (Screen 2) - handled by LoginActivity
        composable("login") {
            LaunchedEffect(Unit) {
                navController.navigate("task_list") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        
        // Task List Screen (Screen 3)
        composable("task_list") {
            TaskListScreen(
                navController = navController, 
                viewModel = taskViewModel,
                onNavigateToAddEdit = {
                    navigationCount++
                    // Show interstitial ad after 3rd screen (before going to 4th screen)
                    if (navigationCount >= 3 && adManager.isInterstitialAdReady()) {
                        adManager.showInterstitialAd(activity) {
                            navController.navigate("add_edit_task")
                        }
                    } else {
                        navController.navigate("add_edit_task")
                    }
                },
                onNavigateToProfile = {
                    navigationCount++
                    navController.navigate("profile")
                }
            )
        }
        
        // Add/Edit Task Screen (Screen 4)
        composable(
            route = "add_edit_task/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            AddEditTaskScreen(
                navController = navController,
                viewModel = taskViewModel,
                taskId = if (taskId == -1) null else taskId
            )
        }
        
        // Simplified route for adding a new task
        composable("add_edit_task") {
            AddEditTaskScreen(
                navController = navController,
                viewModel = taskViewModel,
                taskId = null
            )
        }
        
        // Profile Screen (Screen 5)
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = taskViewModel)
        }
    }
}