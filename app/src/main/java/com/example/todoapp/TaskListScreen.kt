package com.example.todoapp


import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoapp.ui.theme.Black
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RoyalBlue
import com.example.todoapp.ui.theme.White
import com.example.todoapp.ui.theme.LightGray
import com.example.todoapp.ui.theme.HighPriorityColor
import com.example.todoapp.ui.theme.MediumPriorityColor
import com.example.todoapp.ui.theme.LowPriorityColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, viewModel: TaskViewModel) {
    val context = LocalContext.current
    val tasks by viewModel.allTasks.observeAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf<Task?>(null) }
    val userEmail = viewModel.getCurrentUserEmail()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("My Tasks", color = White)
                        Text(
                            text = userEmail,
                            color = White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.logout()
                            context.startActivity(Intent(context, LoginActivity::class.java))
                            (context as? android.app.Activity)?.finishAffinity()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Logout", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_edit_task") },
                containerColor = RoyalBlue,
                contentColor = White
            ) {
                Icon(Icons.Filled.Add, "Add Task")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(DarkBlue, Black)))
                .padding(padding)
        ) {
            if (tasks.isEmpty()) {
                // Show empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = "No Task Icon",
                        modifier = Modifier.size(80.dp),
                        tint = LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Task Added",
                        color = LightGray,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap the + button to add your first task",
                        color = LightGray.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        AnimatedVisibility(
                            visible = true, // You can add logic here for animations
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            TaskItem(
                                task = task,
                                onToggleCompleted = { viewModel.toggleTaskCompleted(task) },
                                onEdit = { navController.navigate("add_edit_task/${task.id}") },
                                onDelete = { showDeleteDialog = task }
                            )
                        }
                    }
                }
            }

            showDeleteDialog?.let { taskToDelete ->
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = null },
                    title = { Text("Delete Task") },
                    text = { Text("Are you sure you want to delete '${taskToDelete.title}'?") },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.deleteTask(taskToDelete)
                            showDeleteDialog = null
                        }) { Text("Confirm") }
                    },
                    dismissButton = {
                        Button(onClick = { showDeleteDialog = null }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggleCompleted: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = "Task Status",
                modifier = Modifier
                    .clickable(onClick = onToggleCompleted)
                    .padding(end = 16.dp),
                tint = if (task.isCompleted) LowPriorityColor else LightGray
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    fontSize = 14.sp,
                    color = LightGray,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriorityIndicator(priority = task.priority)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = task.dueDate, fontSize = 12.sp, color = LightGray)
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = White)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = HighPriorityColor)
                }
            }
        }
    }
}

@Composable
fun PriorityIndicator(priority: Priority) {
    val color = when (priority) {
        Priority.HIGH -> HighPriorityColor
        Priority.MEDIUM -> MediumPriorityColor
        Priority.LOW -> LowPriorityColor
    }
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(color, shape = MaterialTheme.shapes.small)
    )
}