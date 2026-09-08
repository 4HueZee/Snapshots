package com.example.battlebarge

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.battlebarge.engine.EngineCore
import com.example.battlebarge.ui.engine.ArmyListsTab
import com.example.battlebarge.ui.engine.ArmyListsViewModel
import com.example.battlebarge.ui.engine.GameSystemsTab
import com.example.battlebarge.ui.engine.GameSystemsViewModel
import com.example.battlebarge.ui.engine.LibraryHubView

enum class NavigationHub {
    ENGINES,   // 🛡️ Catalogues & Engines Hub
    BUILDER,   // ⚔️ Army Builder Workspace
    LIBRARY    // 🎨 Model Collection & Painting Library System Placeholder
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToSocial: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    var activeHub by remember { mutableStateOf(NavigationHub.ENGINES) }
    var inspectingRosterId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val repository = remember(context) { EngineCore.provideRepository(context) }
    val argonautRepository = remember(context) { EngineCore.provideArgonautRepository(context) }

    val gamesViewModel: GameSystemsViewModel = viewModel(
        factory = GameSystemsViewModel.Factory(repository, context)
    )
    val listsViewModel: ArmyListsViewModel = viewModel(
        factory = ArmyListsViewModel.Factory(repository, argonautRepository)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = "BATTLEBARGE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToSocial) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Social Hub",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAccount) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeHub == NavigationHub.ENGINES,
                    onClick = {
                        inspectingRosterId = null
                        activeHub = NavigationHub.ENGINES
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Catalogues & Engines") },
                    label = { 
                        Text(
                            "ENGINES", 
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        ) 
                    }
                )

                NavigationBarItem(
                    selected = activeHub == NavigationHub.BUILDER,
                    onClick = {
                        activeHub = NavigationHub.BUILDER
                    },
                    icon = { Icon(Icons.Default.Build, contentDescription = "Army Builder") },
                    label = { 
                        Text(
                            "BUILDER", 
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        ) 
                    }
                )

                NavigationBarItem(
                    selected = activeHub == NavigationHub.LIBRARY,
                    onClick = {
                        inspectingRosterId = null
                        activeHub = NavigationHub.LIBRARY
                    },
                    icon = { Icon(Icons.Default.Palette, contentDescription = "Model Library") },
                    label = { 
                        Text(
                            "LIBRARY", 
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        ) 
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeHub) {
                NavigationHub.ENGINES -> {
                    GameSystemsTab(gamesViewModel = gamesViewModel)
                }
                NavigationHub.BUILDER -> {
                    ArmyListsTab(
                        listsViewModel = listsViewModel,
                        inspectingRosterId = inspectingRosterId,
                        onInspectRoster = { inspectingRosterId = it },
                        repository = repository,
                        argonautRepository = argonautRepository
                    )
                }
                NavigationHub.LIBRARY -> {
                    LibraryHubView()
                }
            }
        }
    }
}
