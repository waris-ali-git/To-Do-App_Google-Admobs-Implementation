package com.example.todoapp

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoapp.Priority
import com.example.todoapp.ui.theme.Black
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RoyalBlue
import com.example.todoapp.ui.theme.White
import com.example.todoapp.TaskViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(navController: NavController, viewModel: TaskViewModel, taskId: Int?) {
    val context = LocalContext.current
    val task by viewModel.selectedTask.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("Select Date") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }

    LaunchedEffect(key1 = taskId) {
        if (taskId != null) {
            viewModel.getTaskById(taskId)
        }
    }

    LaunchedEffect(key1 = task) {
        if (taskId != null && task != null) {
            title = task!!.title
            description = task!!.description
            dueDate = task!!.dueDate
            priority = task!!.priority
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task", color = White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(DarkBlue, Black)))
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Date Picker
                DatePickerField(dueDate) { newDate -> dueDate = newDate }

                Spacer(modifier = Modifier.height(16.dp))

                // Priority Selector
                PrioritySelector(selectedPriority = priority, onPrioritySelected = { priority = it })

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        val currentTask = task
                        if (taskId != null && currentTask != null) {
                            viewModel.updateTask(currentTask.copy(title = title, description = description, dueDate = dueDate, priority = priority))
                        } else {
                            viewModel.addTask(title, description, dueDate, priority)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (taskId == null) "Add Task" else "Update Task")
                }
            }
        }
    }
}

@Composable
fun DatePickerField(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onDateSelected("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        readOnly = true,
        label = { Text("Due Date") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, "Select Date", modifier = Modifier.clickable { datePickerDialog.show() })
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PrioritySelector(selectedPriority: Priority, onPrioritySelected: (Priority) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Priority:", color = White)
        Priority.values().forEach { priority ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedPriority == priority,
                    onClick = { onPrioritySelected(priority) }
                )
                Text(priority.name, color = White)
            }
        }
    }
}