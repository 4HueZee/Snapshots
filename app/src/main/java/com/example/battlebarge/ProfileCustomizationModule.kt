package com.example.battlebarge

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * ProfileCustomizationSection provides the UI for editing user profile settings.
 * Refactored to follow MVVM architecture with a dedicated ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCustomizationSection(
    viewModel: ProfileViewModel = viewModel()
) {
    // Collect state from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val username by viewModel.username.collectAsState()
    val bio by viewModel.bio.collectAsState()
    val selectedRegion by viewModel.region.collectAsState()
    val isPublic by viewModel.isPublic.collectAsState()
    
    val regions = remember { listOf("NA", "EU", "Asia", "OCE", "SA", "Africa") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Profile Settings",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Display Name Field
        OutlinedTextField(
            value = username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text("Display Name") },
            placeholder = { Text("Enter your name") },
            singleLine = true,
            maxLines = 1,
            isError = uiState is ProfileUiState.Error && (uiState as ProfileUiState.Error).message.contains("Name"),
            supportingText = {
                if (uiState is ProfileUiState.Error && (uiState as ProfileUiState.Error).message.contains("Name")) {
                    Text(text = (uiState as ProfileUiState.Error).message, color = MaterialTheme.colorScheme.error)
                } else {
                    Text("3-15 alphanumeric characters")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bio Section
        OutlinedTextField(
            value = bio,
            onValueChange = { viewModel.onBioChange(it) },
            label = { Text("Bio") },
            placeholder = { Text("Tell us about yourself...") },
            singleLine = true,
            maxLines = 1,
            isError = uiState is ProfileUiState.Error && (uiState as ProfileUiState.Error).message.contains("Bio"),
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (uiState is ProfileUiState.Error && (uiState as ProfileUiState.Error).message.contains("Bio")) {
                        Text(text = "Invalid bio content", color = MaterialTheme.colorScheme.error)
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Text("${bio.length} / 60")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Region Selection
        Text(
            text = "Interaction Region",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = selectedRegion,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Region") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                regions.forEach { region ->
                    DropdownMenuItem(
                        text = { Text(region) },
                        onClick = {
                            viewModel.onRegionChange(region)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Visibility
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Profile Visibility",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (isPublic) "Visible to others" else "Hidden from search",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isPublic,
                onCheckedChange = { viewModel.onVisibilityChange(it) }
            )
        }

        // --- Status Feedback ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(32.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is ProfileUiState.Loading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
                is ProfileUiState.Success -> Text(
                    text = state.message,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF4CAF50)
                )
                is ProfileUiState.Error -> if (!state.message.contains("Name") && !state.message.contains("Bio")) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {}
            }
        }

        Button(
            onClick = { viewModel.saveChanges() },
            enabled = uiState !is ProfileUiState.Loading,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End)
        ) {
            Text(if (uiState is ProfileUiState.Loading) "Syncing..." else "Save Changes")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
    }
}
