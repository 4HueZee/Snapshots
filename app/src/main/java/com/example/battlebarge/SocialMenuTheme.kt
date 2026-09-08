package com.example.battlebarge

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.example.battlebarge.ui.theme.AppColorScheme
import com.example.battlebarge.ui.theme.BattleBargeTheme
import com.example.battlebarge.ui.theme.ThemePresets

/**
 * Visual identity for Social systems.
 * Homogeneously associated with SocialMenu in FriendsModule.kt.
 */
@Composable
fun SocialMenuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: AppColorScheme = ThemePresets.Imperial,
    content: @Composable () -> Unit
) {
    BattleBargeTheme(
        darkTheme = darkTheme,
        appColorScheme = colorScheme,
        content = content
    )
}
