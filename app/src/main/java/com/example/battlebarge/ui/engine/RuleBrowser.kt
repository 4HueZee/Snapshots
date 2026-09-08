package com.example.battlebarge.ui.engine

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.battlebarge.agnostic.domain.model.Singularity
import com.battlebarge.agnostic.domain.repository.SingularityRepository

/**
 * High-density browser for inspecting rules, codexes, and technical stats.
 * Integrates directly with the Agnostic Relational Engine (ARE) for resolution.
 * Displays Confined Datasheet Cards for Units to separate them from options.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuleBrowser(
    gamesystemId: String,
    factionId: String,
    repository: SingularityRepository,
    onAwaken: (Singularity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Units", "Rules", "Profiles")

    val currentTag = when (selectedTabIndex) {
        0 -> "selectionEntry"
        1 -> "rule"
        2 -> "profile"
        else -> "selectionEntry"
    }

    val nodes by repository.getByTag(gamesystemId, factionId, currentTag).collectAsState(initial = emptyList())
    
    // Filter root units (parentId == null) to prevent mixing units with options
    val rootNodes = remember(nodes, selectedTabIndex) {
        if (selectedTabIndex == 0) {
            nodes.filter { it.parentId == null }
        } else {
            nodes
        }
    }

    var peekingSingularity by remember { mutableStateOf<Singularity?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
            color = MaterialTheme.colorScheme.surface,
            shape = WorkspaceDesign.SharpShape
        ) {
            if (rootNodes.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("NO ENTRIES IN SECTION", color = Color.Gray, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(rootNodes, key = { it.id }) { node ->
                        if (selectedTabIndex == 0) {
                            // Render Root Unit as a Confined Datasheet Card using NodeRenderer
                            NodeRenderer(
                                singularity = node,
                                onAwaken = onAwaken,
                                onPeek = { peekingSingularity = it }
                            )
                        } else {
                            // Resolve Link via ARE for Rules / Profiles
                            val resolved by produceState(initialValue = node, node) {
                                value = repository.resolve(node)
                            }

                            val isLinked = node.targetId != null

                            ListItem(
                                headlineContent = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(resolved.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        if (isLinked) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Default.Link,
                                                contentDescription = "Linked Reference",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .clickable { peekingSingularity = resolved }
                                            )
                                        }
                                    }
                                },
                                supportingContent = {
                                    Column {
                                        resolved.value?.let {
                                            Text(it, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                                        }
                                        if (isLinked && resolved.factionName != node.factionName) {
                                            Text(
                                                text = "SOURCE: ${resolved.factionName.uppercase()}",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 9.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                            HorizontalDivider(color = WorkspaceDesign.IndustrialBorderColor, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }

    peekingSingularity?.let { target ->
        ReferencePreviewDialog(
            singularity = target,
            onDismiss = { peekingSingularity = null }
        )
    }
}

/**
 * Overlay dialog displaying details of a linked reference without leaving the current view.
 */
@Composable
fun ReferencePreviewDialog(
    singularity: Singularity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = WorkspaceDesign.SharpShape,
            modifier = Modifier.fillMaxWidth(0.9f).wrapContentHeight(),
            border = BorderStroke(1.dp, WorkspaceDesign.IndustrialBorderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "REFERENCE DETAIL",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = singularity.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, WorkspaceDesign.IndustrialBorderColor, WorkspaceDesign.SharpShape),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = WorkspaceDesign.SharpShape
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("TAG: ${singularity.xmlTag.uppercase()}", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                        Text("ORIGIN: ${singularity.factionName}", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                        singularity.value?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(it, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = WorkspaceDesign.SharpShape
                ) {
                    Text("DISMISS")
                }
            }
        }
    }
}
