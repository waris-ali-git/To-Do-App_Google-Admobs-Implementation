package com.example.todoapp.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.*

class AdManager private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: AdManager? = null
        
        fun getInstance(): AdManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AdManager().also { INSTANCE = it }
            }
        }
        
        // Test Ad Unit IDs
        const val TEST_APP_OPEN_AD_ID = "ca-app-pub-3940256099942544/9257395921"
        const val TEST_BANNER_AD_ID = "ca-app-pub-3940256099942544/6300978111"
        const val TEST_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712"
        
        private const val TAG = "AdManager"
    }
    
    private var appOpenAd: AppOpenAd? = null
    private var interstitialAd: InterstitialAd? = null
    private var isLoadingAppOpenAd = false
    private var isLoadingInterstitialAd = false
    private var isShowingAd = false
    
    // Ad IDs - will be loaded from Firebase or use test IDs as fallback
    private var appOpenAdId = TEST_APP_OPEN_AD_ID
    private var bannerAdId = TEST_BANNER_AD_ID
    private var interstitialAdId = TEST_INTERSTITIAL_AD_ID
    
    // Firebase Database reference
    private var database: DatabaseReference? = null
    
    fun initialize(context: Context) {
        Log.d(TAG, "Initializing AdManager...")
        MobileAds.initialize(context) { initializationStatus ->
            Log.d(TAG, "AdMob initialized: ${initializationStatus.adapterStatusMap}")
            Log.d(TAG, "Banner Ad ID: $bannerAdId")
            Log.d(TAG, "App Open Ad ID: $appOpenAdId")
            Log.d(TAG, "Interstitial Ad ID: $interstitialAdId")
        }
        
        // Set up lifecycle observer for app open ads
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                if (!isShowingAd) {
                    showAppOpenAd(context as? Activity)
                }
            }
        })
        
        // Load ad IDs from Firebase
        loadAdIdsFromFirebase()
        
        // Pre-load ads
        loadAppOpenAd(context)
        loadInterstitialAd(context)
    }
    
    private fun loadAdIdsFromFirebase() {
        try {
            // Use the specific Firebase database URL
            val firebaseDatabase = FirebaseDatabase.getInstance("https://to-do-app-7ef30-default-rtdb.firebaseio.com/")
            database = firebaseDatabase.getReference("ad_ids")
            database?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    appOpenAdId = snapshot.child("app_open").getValue(String::class.java) ?: TEST_APP_OPEN_AD_ID
                    bannerAdId = snapshot.child("banner").getValue(String::class.java) ?: TEST_BANNER_AD_ID
                    interstitialAdId = snapshot.child("interstitial").getValue(String::class.java) ?: TEST_INTERSTITIAL_AD_ID
                    
                    Log.d(TAG, "Ad IDs loaded from Firebase")
                }
                
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to load ad IDs from Firebase, using test IDs", error.toException())
                }
            })
        } catch (e: Exception) {
            Log.w(TAG, "Firebase not available, using test ad IDs", e)
        }
    }
    
    // App Open Ad
    fun loadAppOpenAd(context: Context) {
        if (isLoadingAppOpenAd || isAppOpenAdAvailable()) {
            return
        }
        
        isLoadingAppOpenAd = true
        val request = AdRequest.Builder().build()
        
        AppOpenAd.load(
            context,
            appOpenAdId,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(TAG, "App open ad loaded")
                    appOpenAd = ad
                    isLoadingAppOpenAd = false
                }
                
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "App open ad failed to load: ${loadAdError.message}")
                    isLoadingAppOpenAd = false
                }
            }
        )
    }
    
    fun showAppOpenAd(activity: Activity?, onAdClosed: (() -> Unit)? = null) {
        if (!isAppOpenAdAvailable() || activity == null) {
            onAdClosed?.invoke()
            return
        }
        
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "App open ad dismissed")
                appOpenAd = null
                isShowingAd = false
                loadAppOpenAd(activity)
                onAdClosed?.invoke()
            }
            
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e(TAG, "App open ad failed to show: ${adError.message}")
                appOpenAd = null
                isShowingAd = false
                onAdClosed?.invoke()
            }
            
            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "App open ad showed")
                isShowingAd = true
            }
        }
        
        appOpenAd?.show(activity)
    }
    
    private fun isAppOpenAdAvailable(): Boolean {
        return appOpenAd != null
    }
    
    // Banner Ad
    fun getBannerAdId(): String = bannerAdId
    
    // Interstitial Ad
    fun loadInterstitialAd(context: Context) {
        if (isLoadingInterstitialAd || interstitialAd != null) {
            return
        }
        
        isLoadingInterstitialAd = true
        val request = AdRequest.Builder().build()
        
        InterstitialAd.load(
            context,
            interstitialAdId,
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded")
                    interstitialAd = ad
                    isLoadingInterstitialAd = false
                }
                
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Interstitial ad failed to load: ${loadAdError.message}")
                    interstitialAd = null
                    isLoadingInterstitialAd = false
                }
            }
        )
    }
    
    fun showInterstitialAd(activity: Activity, onAdClosed: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Interstitial ad dismissed")
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    onAdClosed()
                }
                
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG, "Interstitial ad failed to show: ${adError.message}")
                    interstitialAd = null
                    onAdClosed()
                }
                
                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Interstitial ad showed")
                }
            }
            
            interstitialAd?.show(activity)
        } else {
            Log.d(TAG, "Interstitial ad not ready, proceeding without ad")
            onAdClosed()
        }
    }
    
    fun isInterstitialAdReady(): Boolean {
        return interstitialAd != null
    }
}