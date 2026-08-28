package com.example.battlebarge.ui.theme

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

val Context.dataStore by preferencesDataStore(name = "settings")

enum class AppThemeMode {
    SYSTEM, LIGHT, DARK
}

data class UserPreferences(
    val themeMode: AppThemeMode,
    val colorScheme: AppColorScheme
)

data class AppColorScheme(
    val name: String,
    val primaryLight: Color,
    val secondaryLight: Color,
    val primaryDark: Color,
    val secondaryDark: Color
)

object ThemePresets {
    val Imperial = AppColorScheme(
        name = "Imperial",
        primaryLight = Color(0xFFB8860B), // Dark Goldenrod
        secondaryLight = Color(0xFF8B0000), // Dark Red
        primaryDark = Color(0xFFFFD700), // Gold
        secondaryDark = Color(0xFFFF4500) // Orange Red
    )

    val Void = AppColorScheme(
        name = "Void",
        primaryLight = Color(0xFF6200EE), // Deep Purple
        secondaryLight = Color(0xFF03DAC6), // Teal/Cyan
        primaryDark = Color(0xFFBB86FC), // Light Purple
        secondaryDark = Color(0xFF03DAC6) // Teal/Cyan
    )

    val Warp = AppColorScheme(
        name = "Warp",
        primaryLight = Color(0xFF006400), // Dark Green
        secondaryLight = Color(0xFFBDB76B), // Dark Khaki
        primaryDark = Color(0xFF00FF00), // Lime
        secondaryDark = Color(0xFFFFFF00) // Yellow
    )

    val Crimson = AppColorScheme(
        name = "Crimson",
        primaryLight = Color(0xFF800000), // Maroon
        secondaryLight = Color(0xFF2F4F4F), // Dark Slate Gray
        primaryDark = Color(0xFFFF0000), // Red
        secondaryDark = Color(0xFF008080) // Teal
    )

    // --- Color Blind Friendly Presets ---
    // Using high-contrast palettes (e.g., Okabe-Ito inspired)
    
    val Sorcery = AppColorScheme(
        name = "Sorcery",
        primaryLight = Color(0xFF0072B2), // Deep Blue (Protanopia/Deuteranopia safe)
        secondaryLight = Color(0xFFE69F00), // Orange
        primaryDark = Color(0xFF56B4E9), // Light Blue
        secondaryDark = Color(0xFFF0E442) // Yellow
    )

    val Noise = AppColorScheme(
        name = "Noise",
        primaryLight = Color(0xFFD81B60), // Vibrant Pink
        secondaryLight = Color(0xFFFCE4EC), // Very Light Pink (High contrast for text)
        primaryDark = Color(0xFFF48FB1), // Light Pink
        secondaryDark = Color(0xFF880E4F) // Deep Maroon/Pink (High contrast for text)
    )

    val Corvus = AppColorScheme(
        name = "Corvus",
        primaryLight = Color(0xFF212121), // Charcoal
        secondaryLight = Color(0xFF757575), // Grey
        primaryDark = Color(0xFFE0E0E0), // Light Grey
        secondaryDark = Color(0xFF424242) // Mid Grey
    )

    val all = listOf(Imperial, Void, Warp, Crimson, Sorcery, Noise, Corvus)
}

class ThemeSettings(private val context: Context) {
    private val themeModeKey = stringPreferencesKey("theme_mode")
    private val colorSchemeKey = stringPreferencesKey("color_scheme")

    val preferences: StateFlow<UserPreferences> = context.dataStore.data.map { prefs ->
        val modeName = prefs[themeModeKey] ?: AppThemeMode.SYSTEM.name
        val schemeName = prefs[colorSchemeKey] ?: ThemePresets.Imperial.name

        UserPreferences(
            themeMode = try { AppThemeMode.valueOf(modeName) } catch (e: Exception) { AppThemeMode.SYSTEM },
            colorScheme = ThemePresets.all.find { it.name == schemeName } ?: ThemePresets.Imperial
        )
    }.stateIn(
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
        started = SharingStarted.Eagerly,
        initialValue = UserPreferences(AppThemeMode.SYSTEM, ThemePresets.Imperial)
    )

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { it[themeModeKey] = mode.name }
    }

    suspend fun setColorScheme(scheme: AppColorScheme) {
        context.dataStore.edit { it[colorSchemeKey] = scheme.name }
    }
}
