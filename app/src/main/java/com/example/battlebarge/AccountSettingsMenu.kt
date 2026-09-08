package com.example.battlebarge

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.battlebarge.engine.EngineCore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

enum class SettingsView {
    Home, Profile, Theme, Notifications, Privacy, Help, Upgrade, System
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsMenu(
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
) {
    val profile by UserRepository.userProfileFlow.collectAsState()
    var currentSettingsView by remember { mutableStateOf(SettingsView.Home) }
    
    val user = FirebaseAuth.getInstance().currentUser
    val firestoreUsername = profile?.username

    val userEmail = user?.email ?: "Guest Session"
    val displayName = when {
        !firestoreUsername.isNullOrBlank() -> firestoreUsername
        profile == null -> "Initializing..."
        else -> "Authenticating..."
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (currentSettingsView == SettingsView.Home) "Account" else currentSettingsView.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (currentSettingsView == SettingsView.Home) {
                            onDismiss()
                        } else {
                            currentSettingsView = SettingsView.Home
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Return"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentSettingsView,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "SettingsNavigation",
            modifier = Modifier.padding(innerPadding)
        ) { targetView ->
            Column(modifier = Modifier.fillMaxSize()) {
                if (targetView == SettingsView.Home) {
                    // Header Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (user?.isAnonymous == true) {
                            Text(
                                text = "⚠️ Guest Session: Tap to secure account",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .clickable { currentSettingsView = SettingsView.Upgrade }
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        SettingsHomeView(
                            onNavigate = { currentSettingsView = it },
                            onLogout = {
                                PresenceRepository.setOfflineManually()
                                FirebaseAuth.getInstance().signOut()
                                onLogout()
                                onDismiss()
                            }
                        )
                    }
                } else {
                    // Sub-menu detail views
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (targetView) {
                            SettingsView.Profile -> ProfileCustomizationSection()
                            SettingsView.Theme -> ThemeSettingsSection()
                            SettingsView.Privacy -> PrivacySecuritySection()
                            SettingsView.Help -> HelpSupportSection()
                            SettingsView.Upgrade -> AccountUpgradeSection(onSuccess = { currentSettingsView = SettingsView.Home })
                            SettingsView.System -> SystemSettingsSection()
                            else -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Section under development")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsHomeView(
    onNavigate: (SettingsView) -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            SettingsListItem(
                title = "Profile Settings",
                icon = Icons.Default.Person,
                onClick = { onNavigate(SettingsView.Profile) }
            )
        }
        item {
            SettingsListItem(
                title = "Appearance & Theme",
                icon = Icons.Default.Palette,
                onClick = { onNavigate(SettingsView.Theme) }
            )
        }
        item {
            SettingsListItem(
                title = "Notifications",
                icon = Icons.Default.Notifications,
                onClick = { onNavigate(SettingsView.Notifications) }
            )
        }
        item {
            SettingsListItem(
                title = "Privacy & Security",
                icon = Icons.Default.Lock,
                onClick = { onNavigate(SettingsView.Privacy) }
            )
        }
        item {
            SettingsListItem(
                title = "Help & Support",
                icon = Icons.Default.Info,
                onClick = { onNavigate(SettingsView.Help) }
            )
        }
        item {
            SettingsListItem(
                title = "System & Storage",
                icon = Icons.Default.Settings,
                onClick = { onNavigate(SettingsView.System) }
            )
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            ListItem(
                headlineContent = { 
                    Text(
                        "Log Out", 
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    ) 
                },
                leadingContent = { 
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp, 
                        null,
                        tint = MaterialTheme.colorScheme.error
                    ) 
                },
                modifier = Modifier.clickable { onLogout() }
            )
        }
    }
}

@Composable
fun SettingsListItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = { Icon(icon, null) },
        trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) },
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun SystemSettingsSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val singularityRepo = EngineCore.provideRepository(context)
    val argonautRepo = EngineCore.provideArgonautRepository(context)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Storage & Cache",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Rules Cache",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "This will delete all downloaded .gst and .cat files. You will need to re-download them to browse the codex.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                singularityRepo.clearCache().onSuccess {
                                    snackbarHostState.showSnackbar("Rules cache cleared")
                                }.onFailure {
                                    snackbarHostState.showSnackbar("Error: ${it.message}")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Erase Rules Cache")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "User Database",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "This will delete all your custom rosters and Argonauts. This action is permanent.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                argonautRepo.clearAllUserData().onSuccess {
                                    snackbarHostState.showSnackbar("User database wiped")
                                }.onFailure {
                                    snackbarHostState.showSnackbar("Error: ${it.message}")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Wipe User Database")
                    }
                }
            }
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
