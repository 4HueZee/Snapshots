package com.example.battlebarge

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.example.battlebarge.ui.theme.BattleBargeTheme
import com.example.battlebarge.ui.theme.ThemePresets

/**
 * Visual identity for the Main Dashboard.
 * Homogeneously associated with MainScreen.kt.
 */
@Composable
fun MainScreenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    BattleBargeTheme(
        darkTheme = darkTheme,
        appColorScheme = ThemePresets.Imperial,
        content = content
    )
}
