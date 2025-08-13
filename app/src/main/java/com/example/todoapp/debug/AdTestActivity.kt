package com.example.todoapp.debug

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ads.AdManager
import com.example.todoapp.ads.BannerAdView
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.google.android.gms.ads.*

class AdTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Log.d("AdTest", "Starting Ad Test Activity")
        
        setContent {
            ToDoAppTheme {
                AdTestScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdTestScreen() {
    val context = LocalContext.current
    val adManager = remember { AdManager.getInstance() }
    var logMessages by remember { mutableStateOf(listOf<String>()) }
    
    fun addLog(message: String) {
        Log.d("AdTest", message)
        logMessages = logMessages + "${System.currentTimeMillis() % 100000}: $message"
    }
    
    LaunchedEffect(Unit) {
        addLog("AdTest screen loaded")
        addLog("Banner Ad ID: ${adManager.getBannerAdId()}")
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ad Test Screen") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Ad Integration Test",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Test Banner Ad
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Banner Ad Test:",
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    BannerAdView(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Test Controls
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Ad Manager Info:",
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            addLog("Manual load interstitial ad")
                            adManager.loadInterstitialAd(context)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Load Interstitial Ad")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            addLog("Show interstitial ad - Ready: ${adManager.isInterstitialAdReady()}")
                            if (adManager.isInterstitialAdReady()) {
                                adManager.showInterstitialAd(context as ComponentActivity) {
                                    addLog("Interstitial ad closed")
                                }
                            } else {
                                addLog("Interstitial ad not ready")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Show Interstitial Ad")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            addLog("Manual load app open ad")
                            adManager.loadAppOpenAd(context)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Load App Open Ad")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Log Display
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Debug Logs:",
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    logMessages.takeLast(10).forEach { message ->
                        Text(
                            text = message,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}