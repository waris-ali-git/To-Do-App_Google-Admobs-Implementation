package com.example.todoapp.ads

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adSize: AdSize = AdSize.BANNER
) {
    val context = LocalContext.current
    val adManager = remember { AdManager.getInstance() }
    var isAdLoaded by remember { mutableStateOf(false) }
    var adError by remember { mutableStateOf<String?>(null) }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Gray.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(adSize)
                    adUnitId = adManager.getBannerAdId()
                    
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            Log.d("BannerAd", "Banner ad loaded successfully")
                            isAdLoaded = true
                            adError = null
                        }
                        
                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            Log.e("BannerAd", "Banner ad failed to load: ${loadAdError.message}")
                            adError = loadAdError.message
                            isAdLoaded = false
                        }
                    }
                    
                    Log.d("BannerAd", "Loading banner ad with ID: ${adManager.getBannerAdId()}")
                    val request = AdRequest.Builder().build()
                    loadAd(request)
                }
            }
        )
        
        // Show loading/error overlay when needed
        if (!isAdLoaded && adError == null) {
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading Banner Ad...",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        // Show error if ad failed to load
        if (adError != null) {
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ad Error: $adError",
                        fontSize = 8.sp,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    
    // Try to load ad on first composition
    LaunchedEffect(Unit) {
        Log.d("BannerAd", "Initializing banner ad with ID: ${adManager.getBannerAdId()}")
    }
}