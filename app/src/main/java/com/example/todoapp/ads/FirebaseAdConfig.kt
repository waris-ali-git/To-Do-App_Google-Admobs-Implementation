package com.example.todoapp.ads

import android.util.Log
import com.google.firebase.database.*

object FirebaseAdConfig {
    private const val TAG = "FirebaseAdConfig"
    private var database: DatabaseReference? = null
    
    fun initializeAdIds() {
        try {
            // Use the specific Firebase database URL
            val firebaseDatabase = FirebaseDatabase.getInstance("https://to-do-app-7ef30-default-rtdb.firebaseio.com/")
            database = firebaseDatabase.getReference("ad_ids")
            
            // Set default test ad IDs in Firebase (this would normally be done via Firebase Console)
            val defaultAdIds = mapOf(
                "app_open" to AdManager.TEST_APP_OPEN_AD_ID,
                "banner" to AdManager.TEST_BANNER_AD_ID,
                "interstitial" to AdManager.TEST_INTERSTITIAL_AD_ID
            )
            
            // Check if ad_ids exist, if not, create them with default values
            database?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        // Create the ad_ids node with default test values
                        database?.setValue(defaultAdIds)
                            ?.addOnSuccessListener {
                                Log.d(TAG, "Default ad IDs created in Firebase")
                            }
                            ?.addOnFailureListener { exception ->
                                Log.w(TAG, "Failed to create default ad IDs", exception)
                            }
                    } else {
                        Log.d(TAG, "Ad IDs already exist in Firebase")
                    }
                }
                
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to check ad IDs existence", error.toException())
                }
            })
            
        } catch (e: Exception) {
            Log.w(TAG, "Firebase not available for ad configuration", e)
        }
    }
}