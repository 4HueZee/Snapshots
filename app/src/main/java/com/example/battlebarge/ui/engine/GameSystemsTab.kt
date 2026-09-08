package com.example.battlebarge.ui.engine

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.battlebarge.agnostic.data.local.DownloadedFaction
import com.battlebarge.agnostic.data.local.DownloadedGameSystem
import com.battlebarge.agnostic.data.remote.DiscoveredGame
import com.battlebarge.agnostic.presentation.EngineUpdateStatus
import com.example.battlebarge.engine.EngineCore

/**
 * Screen component for managing installed game rules, repository store, and codex inspection.
 * Refactored to eliminate modal popup dialogs in favor of fluid full-screen viewports.
 */
@Composable
fun GameSystemsTab(
    gamesViewModel: GameSystemsViewModel
) {
    val state by gamesViewModel.uiState.collectAsState()

    when (state.screenMode) {
        EngineScreenMode.INSTALLED_HUB -> {
            InstalledEnginesView(
                state = state,
                onOpenStore = { gamesViewModel.openEngineStore() },
                onToggleSnapshot = { gamesViewModel.toggleHighSpeedMode(it) },
                onInspectSystem = { gamesViewModel.openRulebookInspector(it) },
                onDeleteSystem = { gamesViewModel.deleteGame(it) },
                onUpdateAll = { gamesViewModel.updateAllGames() }
            )
        }
        EngineScreenMode.STORE_GALLERY -> {
            EngineStoreGalleryView(
                state = state,
                onBack = { gamesViewModel.closeEngineStore() },
                onRefresh = { gamesViewModel.refreshLibrary() },
                onInstall = { gamesViewModel.installGame(it) }
            )
        }
        EngineScreenMode.RULEBOOK_INSPECTOR -> {
            val selectedSystem = state.selectedGameForInfo
            if (selectedSystem != null) {
                RulebookInspectorView(
                    system = selectedSystem,
                    catalogues = state.gameCatalogues,
                    onBack = { gamesViewModel.closeRulebookInspector() }
                )
            } else {
                gamesViewModel.closeRulebookInspector()
            }
        }
    }
}

@Composable
fun InstalledEnginesView(
    state: GameSystemsUiState,
    onOpenStore: () -> Unit,
    onToggleSnapshot: (Boolean) -> Unit,
    onInspectSystem: (DownloadedGameSystem) -> Unit,
    onDeleteSystem: (String) -> Unit,
    onUpdateAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Engine Configuration Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .border(0.5.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = WorkspaceDesign.SharpShape
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = if (state.useHighSpeedSnapshots) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "HIGH SPEED SNAPSHOTS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = if (state.useHighSpeedSnapshots) "2s Fast Installs Enabled" else "Standard XML Streaming Active",
                            fontSize = 9.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Switch(
                    checked = state.useHighSpeedSnapshots,
                    onCheckedChange = onToggleSnapshot,
                    modifier = Modifier.scale(0.7f)
                )
            }
        }

        // Main Installed Engines Viewport
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
            color = MaterialTheme.colorScheme.surface,
            shape = WorkspaceDesign.SharpShape
        ) {
            if (state.installedGames.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Download, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.primary, 
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("NO GAME ENGINES INSTALLED", color = Color.Gray, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onOpenStore,
                            shape = WorkspaceDesign.SharpShape
                        ) {
                            Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("OPEN ENGINE STORE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn {
                    items(state.installedGames, key = { it.id }) { system ->
                        val status = state.gameStatuses[system.id] ?: EngineUpdateStatus.UP_TO_DATE
                        
                        ListItem(
                            headlineContent = { Text(system.name, fontWeight = FontWeight.Bold) },
                            supportingContent = {
                                Text(
                                    "ID: ${system.id} • ${status.name}",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = if (status == EngineUpdateStatus.SYNCING) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            },
                            trailingContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedButton(
                                        onClick = { onInspectSystem(system) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = WorkspaceDesign.SharpShape,
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("INSPECT", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(onClick = { onDeleteSystem(system.id) }) {
                                        Icon(
                                            Icons.Default.Delete, 
                                            contentDescription = "Delete Engine",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        )
                        HorizontalDivider(color = WorkspaceDesign.IndustrialBorderColor, thickness = 0.5.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bottom Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onOpenStore,
                modifier = Modifier.weight(1f),
                shape = WorkspaceDesign.SharpShape
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("BROWSE ENGINE STORE", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onUpdateAll,
                enabled = !state.isUpdatingAll && state.installedGames.isNotEmpty(),
                shape = WorkspaceDesign.SharpShape
            ) {
                if (state.isUpdatingAll) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("UPDATE ALL", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EngineStoreGalleryView(
    state: GameSystemsUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onInstall: (DiscoveredGame) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack, shape = WorkspaceDesign.SharpShape) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("INSTALLED ENGINES", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }

            IconButton(onClick = onRefresh, enabled = !state.isLibraryFetching) {
                if (state.isLibraryFetching) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh Library")
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Main Full-Screen Store Gallery
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
            color = MaterialTheme.colorScheme.surface,
            shape = WorkspaceDesign.SharpShape
        ) {
            if (state.isLibraryLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(0.8f).height(4.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeCap = StrokeCap.Square
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("FETCHING REPOSITORY STORE ARCHIVE...", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            } else if (state.allAvailableGames.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("NO RULES FOUND IN GALLERY", color = Color.Gray, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.allAvailableGames) { game ->
                        val isInstalled = state.installedGames.any { installed ->
                            installed.id.equals(game.repoName, ignoreCase = true) ||
                            installed.id.contains(game.repoName, ignoreCase = true) ||
                            game.repoName.contains(installed.id, ignoreCase = true) ||
                            installed.name.equals(game.name, ignoreCase = true)
                        }
                        val isDownloading = state.downloadingGameIds.contains(game.repoName)

                        ListItem(
                            headlineContent = { Text(game.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text(game.repoName, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray) },
                            trailingContent = {
                                if (isDownloading) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("INSTALLING...", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)
                                    }
                                } else if (isInstalled) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = WorkspaceDesign.SharpShape
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircle, 
                                                contentDescription = "Installed", 
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "INSTALLED", 
                                                fontSize = 10.sp, 
                                                fontFamily = FontFamily.Monospace, 
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = { onInstall(game) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                                        shape = WorkspaceDesign.SharpShape,
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("INSTALL", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        )
                        HorizontalDivider(thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun RulebookInspectorView(
    system: DownloadedGameSystem,
    catalogues: List<DownloadedFaction>,
    onBack: () -> Unit
) {
    var activeFactionName by remember { mutableStateOf<String?>(null) }
    val repository = EngineCore.provideRepository()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    if (activeFactionName != null) {
                        activeFactionName = null
                    } else {
                        onBack()
                    }
                },
                shape = WorkspaceDesign.SharpShape
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (activeFactionName != null) "BACK TO FACTIONS" else "BACK TO ENGINES",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = system.name.uppercase(),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Full-Screen Inspector Container
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
            color = MaterialTheme.colorScheme.surface,
            shape = WorkspaceDesign.SharpShape
        ) {
            if (activeFactionName != null) {
                RuleBrowser(
                    gamesystemId = system.id,
                    factionId = activeFactionName!!,
                    repository = repository,
                    onAwaken = {}
                )
            } else {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "INSTALLED FACTION CODEXES (${catalogues.size}):",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (catalogues.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("CORE RULEBOOK INSTALLED", color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(catalogues) { catalogue ->
                                ListItem(
                                    headlineContent = { Text(catalogue.factionName, fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                                    supportingContent = { Text("OPEN CODEX RULEBOOK", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary) },
                                    modifier = Modifier.clickable { activeFactionName = catalogue.factionName }
                                )
                                HorizontalDivider(thickness = 0.5.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}
