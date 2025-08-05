package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.todoapp.NavGraph
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                val auth = FirebaseAuth.getInstance()
                val currentUser by remember { mutableStateOf(auth.currentUser) }
                
                LaunchedEffect(currentUser) {
                    if (currentUser == null) {
                        // User not authenticated, redirect to login
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                }
                
                if (currentUser != null) {
                    NavGraph()
                }
            }
        }
    }
}