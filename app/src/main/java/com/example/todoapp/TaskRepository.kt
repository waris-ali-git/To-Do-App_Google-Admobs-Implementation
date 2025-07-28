package com.example.todoapp

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun getTaskById(taskId: Int): Flow<Task> {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }
}