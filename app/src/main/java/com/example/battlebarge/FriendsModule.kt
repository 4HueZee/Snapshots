package com.example.battlebarge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SocialMenu(
    onDismiss: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<UserProfile>()
    val scope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TopAppBar(
                            title = { Text("Social") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    if (navigator.canNavigateBack()) {
                                        scope.launch { navigator.navigateBack() }
                                    } else {
                                        onDismiss()
                                    }
                                }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface,
                                navigationIconContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        
                        FriendsSection(onFriendClick = { friend ->
                            scope.launch {
                                navigator.navigateTo(androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole.Detail, friend)
                            }
                        })
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                FriendDetail(
                    friend = navigator.currentDestination?.contentKey,
                    onBack = { scope.launch { navigator.navigateBack() } },
                    isDetailOnly = navigator.scaffoldValue[androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden
                )
            }
        }
    )
}

@Composable
fun FriendDetail(
    friend: UserProfile?,
    onBack: () -> Unit,
    isDetailOnly: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isDetailOnly) {
                IconButton(onClick = onBack, modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            }
            
            if (friend == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Select an Operative", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = friend.username,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "ID: ${friend.uid}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Bio", style = MaterialTheme.typography.titleSmall)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = if (friend.bio.isNotBlank()) friend.bio else "No records found.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val statusColor = when (friend.presenceStatus) {
                            PresenceConstants.ONLINE -> Color(0xFF4CAF50)
                            PresenceConstants.AWAY -> Color(0xFFFFC107)
                            PresenceConstants.RECENTLY_SEEN -> Color(0xFFFF9800)
                            else -> Color.Gray
                        }
                        Surface(
                            modifier = Modifier.size(12.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = statusColor
                        ) {}
                        Spacer(Modifier.width(8.dp))
                        val statusText = when (friend.presenceStatus) {
                            PresenceConstants.ONLINE -> "Active in the Barge"
                            PresenceConstants.AWAY -> "Away"
                            PresenceConstants.RECENTLY_SEEN -> "Recently Seen"
                            else -> "Offline"
                        }
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FriendsSection(
    onFriendClick: (UserProfile) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<UserProfile?>(null) }
    var isSearching by remember { mutableStateOf(false) }
    
    val friends by FriendsRepository.friendsFlow.collectAsState(initial = emptyList())
    val requests by FriendsRepository.requestsFlow.collectAsState(initial = emptyList())
    
    var message by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it 
                    if (it.isBlank()) searchResult = null
                },
                label = { Text("Find Operative by Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            scope.launch {
                                isSearching = true
                                searchResult = FriendsRepository.searchUserByUsername(searchQuery)
                                isSearching = false
                                if (searchResult == null) {
                                    message = "No record found"
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )

            if (isSearching) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))
            }
        }

        // Search Result Item
        searchResult?.let { user ->
            item {
                ListItem(
                    headlineContent = { Text(user.username) },
                    leadingContent = { Icon(Icons.Default.PersonAdd, null) },
                    trailingContent = {
                        Button(onClick = {
                            scope.launch {
                                val result = FriendsRepository.sendFriendRequest(user.uid, user.username)
                                if (result.isSuccess) {
                                    message = "Request transmitted."
                                    searchResult = null
                                    searchQuery = ""
                                } else {
                                    message = result.exceptionOrNull()?.message
                                }
                            }
                        }) {
                            Text("Add")
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (message != null) {
            item {
                Text(
                    text = message!!,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (message!!.contains("transmitted")) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // Pending Requests Header
        if (requests.isNotEmpty()) {
            item {
                Text(
                    text = "Pending Requests",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
            items(requests, key = { it.id }) { request ->
                ListItem(
                    headlineContent = { Text(request.senderUsername) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = {
                                scope.launch {
                                    FriendsRepository.respondToRequest(request.id, true)
                                }
                            }) {
                                Icon(Icons.Default.Check, null, tint = Color(0xFF4CAF50))
                            }
                            IconButton(onClick = {
                                scope.launch {
                                    FriendsRepository.respondToRequest(request.id, false)
                                }
                            }) {
                                Icon(Icons.Default.Clear, null, tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Friends List Header
        item {
            Text(
                text = "Connected Operatives (${friends.size})",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        if (friends.isEmpty()) {
            item {
                Text(
                    text = "No connections established.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            items(friends, key = { it.uid }) { friend ->
                ListItem(
                    headlineContent = { 
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(friend.username)
                            val statusColor = when (friend.presenceStatus) {
                                PresenceConstants.ONLINE -> Color(0xFF4CAF50)
                                PresenceConstants.AWAY -> Color(0xFFFFC107)
                                PresenceConstants.RECENTLY_SEEN -> Color(0xFFFF9800)
                                else -> null
                            }
                            if (statusColor != null) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    modifier = Modifier.size(8.dp),
                                    shape = androidx.compose.foundation.shape.CircleShape,
                                    color = statusColor
                                ) {}
                            }
                        }
                    },
                    supportingContent = {
                        val subText = when (friend.presenceStatus) {
                            PresenceConstants.RECENTLY_SEEN -> "Recently seen"
                            PresenceConstants.AWAY -> "Away"
                            PresenceConstants.OFFLINE -> friend.lastActive?.let { "Last active: ${formatTimestamp(it)}" }
                            else -> null
                        }
                        if (subText != null) {
                            Text(
                                text = subText,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable { onFriendClick(friend) }
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: com.google.firebase.Timestamp): String {
    val seconds = timestamp.seconds
    val now = com.google.firebase.Timestamp.now().seconds
    val diff = now - seconds
    
    return when {
        diff < 60 -> "just now"
        diff < 3600 -> "${diff / 60}m ago"
        diff < 86400 -> "${diff / 3600}h ago"
        else -> "${diff / 86400}d ago"
    }
}
