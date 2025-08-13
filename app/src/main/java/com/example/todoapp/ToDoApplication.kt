package com.example.todoapp

import android.app.Application
import com.example.todoapp.ads.AdManager
import com.example.todoapp.ads.FirebaseAdConfig
import com.google.firebase.FirebaseApp

class ToDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        
        // Initialize AdManager early in Application class
        AdManager.getInstance().initialize(this)
        
        // Initialize Firebase Ad Configuration
        FirebaseAdConfig.initializeAdIds()
    }
} 