package com.example.battlebarge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * Theming driven by User Social Identity.
 * Consumers user preferences for color schemes (Imperial, Warp, etc).
 */
@Composable
fun SocialIdentityTheme(
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
