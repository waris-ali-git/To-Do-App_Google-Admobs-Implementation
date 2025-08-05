package com.example.todoapp

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasksForUser(userId: String): Flow<List<Task>> = taskDao.getAllTasksForUser(userId)

    fun getTaskByIdForUser(taskId: Int, userId: String): Flow<Task> {
        return taskDao.getTaskByIdForUser(taskId, userId)
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