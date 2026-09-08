package com.example.battlebarge.ui.engine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A custom layout shell for the Agnostic Workspace.
 * Manual structure: Decoupled from Material 3 Scaffold to allow 
 * high-density positioning (e.g. toggles beside camera lens).
 */
@Composable
fun AgnosticShell(
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    // We manually handle system bars here to bypass Scaffold's forced padding logic
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // The Header Area: Sits right at the top, manually accounting for status bar
        Surface(
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = systemBarsPadding.calculateTopPadding())
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth()
            ) {
                topBar()
            }
        }
        
        // The Main Viewport: Manual control over the remaining space
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            content(
                PaddingValues(
                    bottom = systemBarsPadding.calculateBottomPadding(),
                    start = 0.dp,
                    end = 0.dp
                )
            )
        }
    }
}
