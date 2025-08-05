package com.example.todoapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    private val auth = FirebaseAuth.getInstance()

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: LiveData<List<Task>> = _allTasks.asLiveData()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        loadUserTasks()
    }

    private fun loadUserTasks() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                repository.getAllTasksForUser(currentUser.uid).collect { tasks ->
                    _allTasks.value = tasks
                }
            }
        }
    }

    fun getTaskById(id: Int) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                _selectedTask.value = repository.getTaskByIdForUser(id, currentUser.uid).first()
            }
        }
    }

    fun addTask(title: String, description: String, dueDate: String, priority: Priority) = viewModelScope.launch {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val newTask = Task(
                title = title, 
                description = description, 
                dueDate = dueDate, 
                priority = priority,
                userId = currentUser.uid
            )
            repository.insert(newTask)
        }
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        val currentUser = auth.currentUser
        if (currentUser != null && task.userId == currentUser.uid) {
            repository.update(task)
        }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        val currentUser = auth.currentUser
        if (currentUser != null && task.userId == currentUser.uid) {
            repository.delete(task)
        }
    }

    fun toggleTaskCompleted(task: Task) = viewModelScope.launch {
        val currentUser = auth.currentUser
        if (currentUser != null && task.userId == currentUser.uid) {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            repository.update(updatedTask)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }
}