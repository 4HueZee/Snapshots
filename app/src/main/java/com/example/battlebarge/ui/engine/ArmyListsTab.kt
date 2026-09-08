package com.example.battlebarge.ui.engine

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.battlebarge.agnostic.data.local.DownloadedGameSystem
import com.battlebarge.agnostic.data.remote.DiscoveredSource
import com.battlebarge.agnostic.domain.repository.ArgonautRepository
import com.battlebarge.agnostic.domain.repository.SingularityRepository

/**
 * Screen component for managing user-created army lists using modular ViewModels.
 * Refactored to eliminate popup dialogs in favor of full-screen wizard viewports.
 */
@Composable
fun ArmyListsTab(
    listsViewModel: ArmyListsViewModel,
    inspectingRosterId: String?,
    onInspectRoster: (String?) -> Unit,
    repository: SingularityRepository,
    argonautRepository: ArgonautRepository
) {
    val listsUiState by listsViewModel.uiState.collectAsState()

    if (inspectingRosterId != null) {
        val detailViewModel: ArmyListDetailViewModel = viewModel(
            key = inspectingRosterId,
            factory = ArmyListDetailViewModel.Factory(inspectingRosterId, repository, argonautRepository)
        )
        ArmyListDetailView(
            rosterId = inspectingRosterId,
            detailViewModel = detailViewModel
        )
    } else {
        when (listsUiState.screenMode) {
            RosterScreenMode.LIBRARY -> {
                RosterLibraryView(
                    state = listsUiState,
                    onInspectRoster = onInspectRoster,
                    onOpenWizard = { listsViewModel.openCreationWizard() },
                    onDeleteRoster = { listsViewModel.deleteArmyList(it) },
                    onExportRoster = { listsViewModel.exportArmyList(it) }
                )
            }
            RosterScreenMode.CREATION_WIZARD -> {
                FullscreenRosterWizardView(
                    state = listsUiState,
                    onBack = { listsViewModel.closeCreationWizard() },
                    onSystemSelected = { listsViewModel.selectSystemForNewList(it) },
                    onFactionSelected = { listName, system, faction ->
                        listsViewModel.createArmyList(listName, system, faction) { newRosterId ->
                            onInspectRoster(newRosterId)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RosterLibraryView(
    state: ArmyListsUiState,
    onInspectRoster: (String) -> Unit,
    onOpenWizard: () -> Unit,
    onDeleteRoster: (String) -> Unit,
    onExportRoster: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
            color = MaterialTheme.colorScheme.surface,
            shape = WorkspaceDesign.SharpShape
        ) {
            if (state.armyLists.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Build, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.primary, 
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("NO ARMY LISTS DETECTED", color = Color.Gray, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onOpenWizard,
                            shape = WorkspaceDesign.SharpShape
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("CREATE COMMAND LIST", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn {
                    items(state.armyLists, key = { it.id }) { roster ->
                        var showMenu by remember { mutableStateOf(false) }

                        ListItem(
                            headlineContent = { Text(roster.name, fontWeight = FontWeight.Bold) },
                            supportingContent = {
                                Text(
                                    "FACTION: ${roster.factionId.uppercase()} • ${roster.pointsLimit} PTS",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                )
                            },
                            modifier = Modifier.clickable { onInspectRoster(roster.id) },
                            trailingContent = {
                                Box {
                                    IconButton(onClick = { showMenu = true }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "Roster Options")
                                    }
                                    DropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Export Roster", fontFamily = FontFamily.Monospace, fontSize = 12.sp) },
                                            leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                showMenu = false
                                                onExportRoster(roster.id)
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Delete Roster", color = MaterialTheme.colorScheme.error, fontFamily = FontFamily.Monospace, fontSize = 12.sp) },
                                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                showMenu = false
                                                onDeleteRoster(roster.id)
                                            }
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

        Button(
            onClick = onOpenWizard,
            modifier = Modifier.fillMaxWidth(),
            shape = WorkspaceDesign.SharpShape
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("CREATE NEW COMMAND LIST", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FullscreenRosterWizardView(
    state: ArmyListsUiState,
    onBack: () -> Unit,
    onSystemSelected: (DownloadedGameSystem) -> Unit,
    onFactionSelected: (String, DownloadedGameSystem, DiscoveredSource) -> Unit
) {
    var listName by remember { mutableStateOf("New Command List") }

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
                Text("CANCEL WIZARD", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }

            Text(
                text = if (state.selectedSystemForNewList == null) "STEP 1: SELECT ENGINE" else "STEP 2: SELECT FACTION",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
            color = MaterialTheme.colorScheme.surface,
            shape = WorkspaceDesign.SharpShape
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (state.selectedSystemForNewList == null) {
                    // Step 1: System Select
                    if (state.installedGames.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    Icons.Default.Download, 
                                    contentDescription = null, 
                                    tint = MaterialTheme.colorScheme.primary, 
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "NO GAME ENGINES INSTALLED", 
                                    fontWeight = FontWeight.Bold, 
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Please install a game engine first from the ENGINES tab to begin creating army lists.",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(state.installedGames) { system ->
                                ListItem(
                                    headlineContent = { Text(system.name, fontWeight = FontWeight.Bold) },
                                    modifier = Modifier.clickable { onSystemSelected(system) },
                                    trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) }
                                )
                                HorizontalDivider(thickness = 0.5.dp)
                            }
                        }
                    }
                } else {
                    // Step 2: Faction Select
                    OutlinedTextField(
                        value = listName,
                        onValueChange = { listName = it },
                        label = { Text("List Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = WorkspaceDesign.SharpShape,
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier.weight(1f).border(0.5.dp, Color.Gray, WorkspaceDesign.SharpShape),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ) {
                        if (state.isFactionsLoading) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    LinearProgressIndicator(
                                        modifier = Modifier.fillMaxWidth(0.8f).height(4.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        strokeCap = StrokeCap.Square
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "DISCOVERING FACTIONS...",
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        } else if (state.availableFactionsForNewList.isEmpty()) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("NO FACTIONS DISCOVERED", color = Color.Gray, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(state.availableFactionsForNewList) { faction ->
                                    ListItem(
                                        headlineContent = { Text(faction.name, fontWeight = FontWeight.Bold) },
                                        modifier = Modifier.clickable {
                                            onFactionSelected(listName, state.selectedSystemForNewList, faction)
                                        }
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
}
