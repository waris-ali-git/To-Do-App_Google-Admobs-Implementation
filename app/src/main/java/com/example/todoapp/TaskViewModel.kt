package com.example.todoapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    val allTasks: LiveData<List<Task>>

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks.asLiveData()
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            _selectedTask.value = repository.getTaskById(id).first()
        }
    }

    fun addTask(title: String, description: String, dueDate: String, priority: Priority) = viewModelScope.launch {
        val newTask = Task(title = title, description = description, dueDate = dueDate, priority = priority)
        repository.insert(newTask)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun toggleTaskCompleted(task: Task) = viewModelScope.launch {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        repository.update(updatedTask)
    }
}