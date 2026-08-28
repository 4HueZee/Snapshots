package com.example.battlebarge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.battlebarge.ui.theme.AppThemeMode
import com.example.battlebarge.ui.theme.ThemePresets
import com.example.battlebarge.ui.theme.ThemeSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val themeSettings = remember { ThemeSettings(context) }
    
    val prefs by themeSettings.preferences.collectAsState(initial = null)
    
    val currentMode = prefs?.themeMode ?: AppThemeMode.SYSTEM
    val currentScheme = prefs?.colorScheme ?: ThemePresets.Imperial

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Active Scheme Name
            Text(
                text = "${currentScheme.name} Theme",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Theme Mode - Compact Icon Segmented Button
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.width(160.dp)
            ) {
                AppThemeMode.entries.forEachIndexed { index, mode ->
                    val icon = when (mode) {
                        AppThemeMode.SYSTEM -> Icons.Default.Settings
                        AppThemeMode.LIGHT -> Icons.Default.LightMode
                        AppThemeMode.DARK -> Icons.Default.DarkMode
                    }
                    
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = AppThemeMode.entries.size),
                        onClick = { scope.launch { themeSettings.setThemeMode(mode) } },
                        selected = currentMode == mode,
                        icon = {}, // Remove the checkmark to keep it minimalist
                        label = { 
                            Icon(
                                imageVector = icon,
                                contentDescription = mode.name,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }

        // Color Presets - Small Minimalist Circles
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(ThemePresets.all) { scheme ->
                val isSelected = currentScheme.name == scheme.name
                val isDark = isSystemInDarkTheme()
                
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDark) scheme.primaryDark else scheme.primaryLight)
                        .clickable { scope.launch { themeSettings.setColorScheme(scheme) } }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        // Reveal secondary color in the middle when selected
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isDark) scheme.secondaryDark else scheme.secondaryLight)
                        )
                    }
                }
            }
        }
    }
}
