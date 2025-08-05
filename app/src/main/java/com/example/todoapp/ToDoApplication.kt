package com.example.todoapp

import android.app.Application
import com.google.firebase.FirebaseApp

class ToDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
} 