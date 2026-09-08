package com.example.battlebarge.ui.engine

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
 * Design Constants for the Industrial Hub.
 */
object WorkspaceDesign {
    val SharpShape = RoundedCornerShape(6.dp)
    val IndustrialBorderColor = Color(0xFF4A4D52)
}

/**
 * The technical design system for the Workspace Engine.
 * Homogeneously associated with Workspace.kt.
 */
@Composable
fun WorkspaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    userHighlightColor: Color? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = userHighlightColor ?: Color(0xFF64B5F6),
            secondary = Color(0xFF90A4AE),
            tertiary = Color(0xFF4FC3F7),
            background = Color(0xFF0F172A),
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
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            letterSpacing = 0.5.sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = agnosticTypography,
        shapes = Shapes(
            extraSmall = WorkspaceDesign.SharpShape,
            small = WorkspaceDesign.SharpShape,
            medium = WorkspaceDesign.SharpShape,
            large = WorkspaceDesign.SharpShape,
            extraLarge = WorkspaceDesign.SharpShape
        ),
        content = content
    )
}
