package com.example.battlebarge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.battlebarge.engine.EngineCore
import com.example.battlebarge.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The Anchor of the Battle Barge experience.
 * Primary application shell hosting the top-level NavigationBar Scaffold.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeSettings = ThemeSettings(this)
        
        // Initialize background tasks asynchronously to prevent main-thread launch hang
        CoroutineScope(Dispatchers.IO).launch {
            try {
                PresenceRepository.initialize()
                EngineCore.initialize(applicationContext)
            } catch (e: Exception) {
                Log.e("MainActivity", "Async initialization error", e)
            }
        }
        
        try {
            startService(Intent(this, PresenceService::class.java))
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to start PresenceService", e)
        }

        setContent {
            val prefs by themeSettings.preferences.collectAsState()
            
            val darkTheme = when (prefs.themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }

            val navController = rememberNavController()
            var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

            DisposableEffect(Unit) {
                val auth = FirebaseAuth.getInstance()
                val listener = FirebaseAuth.AuthStateListener { 
                    currentUser = it.currentUser
                }
                auth.addAuthStateListener(listener)
                onDispose {
                    auth.removeAuthStateListener(listener)
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(), 
                color = Color.Black 
            ) {
                if (currentUser == null) {
                    MainScreenTheme(darkTheme = darkTheme) {
                        Scaffold { innerPadding ->
                            LoginScreen(modifier = Modifier.padding(innerPadding))
                        }
                    }
                } else {
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainScreenTheme(darkTheme = darkTheme) {
                                MainScreen(
                                    onNavigateToSocial = { navController.navigate("social") },
                                    onNavigateToAccount = { navController.navigate("account") }
                                )
                            }
                        }
                        composable("social") {
                            SocialMenuTheme(darkTheme = darkTheme, colorScheme = prefs.colorScheme) {
                                SocialMenu(onDismiss = { navController.popBackStack() })
                            }
                        }
                        composable("account") {
                            SocialMenuTheme(darkTheme = darkTheme, colorScheme = prefs.colorScheme) {
                                AccountSettingsMenu(
                                    onDismiss = { navController.popBackStack() },
                                    onLogout = { /* Handled by AuthStateListener */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
