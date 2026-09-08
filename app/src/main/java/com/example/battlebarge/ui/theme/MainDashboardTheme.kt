package com.example.battlebarge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * The default visual identity for the Battle Barge Dashboard.
 * Clean, spacious, and welcoming.
 */
@Composable
fun MainDashboardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For now, we delegate to the existing BattleBargeTheme 
    // but pass default params to keep it as the "standard" identity.
    BattleBargeTheme(
        darkTheme = darkTheme,
        appColorScheme = ThemePresets.Imperial, // Default identity
        content = content
    )
}
