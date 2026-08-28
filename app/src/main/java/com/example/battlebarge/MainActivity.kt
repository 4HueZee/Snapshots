package com.example.battlebarge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.battlebarge.ui.theme.AppThemeMode
import com.example.battlebarge.ui.theme.BattleBargeTheme
import com.example.battlebarge.ui.theme.ThemeSettings
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeSettings = ThemeSettings(this)
        PresenceRepository.initialize()
        
        startService(android.content.Intent(this, PresenceService::class.java))

        setContent {
            val prefs by themeSettings.preferences.collectAsState()
            
            val darkTheme = when (prefs.themeMode) {
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            }

            BattleBargeTheme(
                darkTheme = darkTheme,
                appColorScheme = prefs.colorScheme
            ) {
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

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (currentUser == null) {
                        Scaffold { innerPadding ->
                            LoginScreen(modifier = Modifier.padding(innerPadding))
                        }
                    } else {
                        NavHost(navController = navController, startDestination = "main") {
                            composable("main") {
                                MainScreen(
                                    onNavigateToSocial = { navController.navigate("social") },
                                    onNavigateToAccount = { navController.navigate("account") }
                                )
                            }
                            composable("social") {
                                SocialMenu(onDismiss = { navController.popBackStack() })
                            }
                            composable("account") {
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
