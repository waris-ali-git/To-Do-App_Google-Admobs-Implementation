package com.example.todoapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.AddEditTaskScreen
import com.example.todoapp.TaskListScreen
import com.example.todoapp.TaskViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = viewModel()

    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            TaskListScreen(navController = navController, viewModel = taskViewModel)
        }
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
    }
}