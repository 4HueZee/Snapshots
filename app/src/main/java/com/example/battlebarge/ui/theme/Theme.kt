package com.example.battlebarge.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/* 
 * These were the default color schemes. 
 * They are currently superseded by the dynamic BattleBargeTheme engine.
 */

@Composable
fun BattleBargeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    appColorScheme: AppColorScheme = ThemePresets.Imperial,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = remember(darkTheme, dynamicColor, appColorScheme) {
        // ... existing color scheme logic ...
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> {
                val primary = appColorScheme.primaryDark
                val secondary = appColorScheme.secondaryDark
                val surface = Color(0xFF121212)
                
                val primaryContainer = primary.copy(alpha = 0.25f)
                val secondaryContainer = secondary.copy(alpha = 0.25f)
                
                darkColorScheme(
                    primary = primary,
                    onPrimary = if (primary.luminance() > 0.45f) Color.Black else Color.White,
                    primaryContainer = primaryContainer,
                    onPrimaryContainer = Color.White,
                    
                    secondary = secondary,
                    onSecondary = if (secondary.luminance() > 0.45f) Color.Black else Color.White,
                    secondaryContainer = secondaryContainer,
                    onSecondaryContainer = Color.White,

                    tertiary = secondary.copy(alpha = 0.7f),
                    
                    surface = surface,
                    onSurface = Color.White,
                    surfaceVariant = Color(0xFF1E1E1E),
                    onSurfaceVariant = Color(0xFFB0B0B0),
                    
                    background = surface,
                    onBackground = Color.White,
                    
                    error = Color(0xFFCF6679),
                    onError = Color.Black
                )
            }
            else -> {
                val primary = appColorScheme.primaryLight
                val secondary = appColorScheme.secondaryLight
                val surface = Color(0xFFFDFDFD)
                
                val primaryContainer = primary.copy(alpha = 0.12f)
                val secondaryContainer = secondary.copy(alpha = 0.12f)

                lightColorScheme(
                    primary = primary,
                    onPrimary = if (primary.luminance() > 0.45f) Color.Black else Color.White,
                    primaryContainer = primaryContainer,
                    onPrimaryContainer = Color.Black,

                    secondary = secondary,
                    onSecondary = if (secondary.luminance() > 0.45f) Color.Black else Color.White,
                    secondaryContainer = secondaryContainer,
                    onSecondaryContainer = Color.Black,

                    tertiary = secondary.copy(alpha = 0.7f),
                    
                    surface = surface,
                    onSurface = Color(0xFF1C1B1F),
                    surfaceVariant = Color(0xFFF0F0F0),
                    onSurfaceVariant = Color(0xFF49454F),

                    background = surface,
                    onBackground = Color(0xFF1C1B1F),
                    
                    error = Color(0xFFB00020),
                    onError = Color.White
                )
            }
        }
    }

    val shapes = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(28.dp)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}
