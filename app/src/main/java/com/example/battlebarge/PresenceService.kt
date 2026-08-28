package com.example.battlebarge

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * PresenceService handles the "OFFLINE" status when the app is explicitly closed
 * by the user (swiped away from recents).
 */
class PresenceService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("PresenceService", "App swiped away - setting user to OFFLINE")
        PresenceRepository.setOfflineManually()
        
        // Give some time for the Firestore write to trigger before process dies
        Thread.sleep(500)
        
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }
}
