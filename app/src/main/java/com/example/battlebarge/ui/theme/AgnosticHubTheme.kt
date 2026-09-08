package com.example.battlebarge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A dedicated, high-density theme for the Agnostic Hub.
 * Design Identity: "Steel & Slate" - Technical, sharp, and data-focused.
 * Decoupled from the primary app's social sculpture.
 */
@Composable
fun AgnosticHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    userHighlightColor: Color? = null, // Optional user color injection for accents only
    content: @Composable () -> Unit
) {
    val steel = Color(0xFF2C3E50)
    val slate = Color(0xFF34495E)
    val technicalGray = Color(0xFFB0BEC5)
    
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = userHighlightColor ?: Color(0xFF64B5F6), // Technical Blue if no user color
            secondary = Color(0xFF90A4AE),
            tertiary = Color(0xFF4FC3F7),
            background = Color(0xFF0F172A), // Deep Space Blue/Black
            surface = Color(0xFF1E293B),
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color(0xFFF1F5F9),
            onSurface = Color(0xFFF1F5F9),
            surfaceVariant = Color(0xFF334155),
            onSurfaceVariant = Color(0xFF94A3B8)
        )
    } else {
        lightColorScheme(
            primary = userHighlightColor ?: Color(0xFF1976D2),
            secondary = Color(0xFF455A64),
            tertiary = Color(0xFF0288D1),
            background = Color(0xFFF8FAFC),
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF0F172A),
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFE2E8F0),
            onSurfaceVariant = Color(0xFF64748B)
        )
    }

    val agnosticTypography = Typography(
        displayLarge = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = 1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp, // Tighter for density
            lineHeight = 20.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 0.5.sp
        )
    )

    val sharpShapes = Shapes(
        extraSmall = RoundedCornerShape(0.dp),
        small = RoundedCornerShape(1.dp),
        medium = RoundedCornerShape(2.dp),
        large = RoundedCornerShape(4.dp),
        extraLarge = RoundedCornerShape(8.dp)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = agnosticTypography,
        shapes = sharpShapes,
        content = content
    )
}
