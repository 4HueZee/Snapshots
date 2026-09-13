package com.example.battlebarge

import android.content.Context
import com.example.battlebarge.engine.EngineCore

/**
 * A dedicated modular failsafe for the testing phase.
 * Ensures a "Clean Slate" environment by wiping all local data on boot.
 */
object TestingFailsafe {
    
    /**
     * TOGGLE: Set to 'true' to purge all databases every time the app reloads.
     * Set to 'false' for persistent data during stable testing.
     */
    const val NUCLEAR_WASH_ON_BOOT = true

    /**
     * Executes the data purge if the failsafe is enabled.
     * Separated from core logic to prevent code tangling.
     */
    fun checkAndPerformPurge(context: Context) {
        if (NUCLEAR_WASH_ON_BOOT) {
            android.util.Log.d("Failsafe", "NUCLEAR WASH INITIATED: Wiping all local data.")
            
            // 1. Wipe Agnostic Engine (Eden Database)
            EngineCore.nuclearReset(context)
            
            // 2. Wipe User Repository Cache
            UserRepository.clearCachedProfile()
            
            // 3. Clear App Preferences (DataStore) if necessary
            // context.dataStore.edit { it.clear() }
        }
    }
}
