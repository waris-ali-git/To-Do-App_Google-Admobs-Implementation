package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Priority {
    HIGH, MEDIUM, LOW
}

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: Priority,
    val isCompleted: Boolean = false,
    val userId: String = "" // Add user ID to separate tasks by user
)
