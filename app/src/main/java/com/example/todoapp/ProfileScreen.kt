package com.example.todoapp

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoapp.ads.BannerAdView
import com.example.todoapp.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: TaskViewModel) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userEmail = currentUser?.email ?: "Unknown User"
    val userName = currentUser?.displayName ?: userEmail.substringBefore("@")
    
    // Task statistics
    val allTasks by viewModel.allTasks.observeAsState(initial = emptyList())
    val completedTasks = allTasks.count { it.isCompleted }
    val pendingTasks = allTasks.count { !it.isCompleted }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoyalBlue)
            )
        },
        bottomBar = {
            BannerAdView()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(RoyalBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.firstOrNull()?.uppercase() ?: "U",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // User Name
                    Text(
                        text = userName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    
                    // User Email
                    Text(
                        text = userEmail,
                        fontSize = 14.sp,
                        color = Black.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Task Statistics
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Task Statistics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatisticItem(
                            title = "Total Tasks",
                            value = allTasks.size.toString(),
                            icon = Icons.Filled.Assignment,
                            color = RoyalBlue
                        )
                        
                        StatisticItem(
                            title = "Completed",
                            value = completedTasks.toString(),
                            icon = Icons.Filled.CheckCircle,
                            color = Color(0xFF4CAF50)
                        )
                        
                        StatisticItem(
                            title = "Pending",
                            value = pendingTasks.toString(),
                            icon = Icons.Filled.Schedule,
                            color = Color(0xFFFF9800)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Settings Options
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Settings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    SettingsItem(
                        title = "Notifications",
                        subtitle = "Manage task reminders",
                        icon = Icons.Filled.Notifications,
                        onClick = { /* TODO: Implement notifications settings */ }
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    SettingsItem(
                        title = "Theme",
                        subtitle = "Choose your preferred theme",
                        icon = Icons.Filled.Palette,
                        onClick = { /* TODO: Implement theme settings */ }
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    SettingsItem(
                        title = "About",
                        subtitle = "App version and information",
                        icon = Icons.Filled.Info,
                        onClick = { /* TODO: Implement about screen */ }
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    SettingsItem(
                        title = "Ad Test (Debug)",
                        subtitle = "Test ad integration",
                        icon = Icons.Filled.BugReport,
                        onClick = {
                            context.startActivity(
                                Intent(context, com.example.todoapp.debug.AdTestActivity::class.java)
                            )
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Logout Button
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Logout",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFD32F2F)
                        )
                        Text(
                            text = "Sign out from your account",
                            fontSize = 12.sp,
                            color = Color(0xFFD32F2F).copy(alpha = 0.7f)
                        )
                    }
                    
                    TextButton(
                        onClick = {
                            viewModel.logout()
                            context.startActivity(Intent(context, LoginActivity::class.java))
                            (context as? android.app.Activity)?.finishAffinity()
                        }
                    ) {
                        Text(
                            "Sign Out",
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatisticItem(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        
        Text(
            text = title,
            fontSize = 12.sp,
            color = Black.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = RoyalBlue,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Black
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Black.copy(alpha = 0.6f)
            )
        }
        
        IconButton(onClick = onClick) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open $title",
                tint = Black.copy(alpha = 0.6f)
            )
        }
    }
}