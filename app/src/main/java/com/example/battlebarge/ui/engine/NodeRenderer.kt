package com.example.battlebarge.ui.engine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.battlebarge.agnostic.domain.model.Singularity
import com.example.battlebarge.engine.EngineCore

/**
 * A recursive renderer for the rule tree.
 * Refactored for Relational Intelligence and link peeking.
 */
@Composable
fun NodeRenderer(
    singularity: Singularity,
    onAwaken: (Singularity) -> Unit,
    modifier: Modifier = Modifier,
    children: List<Singularity>? = null,
    onPeek: ((Singularity) -> Unit)? = null
) {
    val isUnit = singularity.xmlTag.contains("selection", ignoreCase = true) || singularity.xmlTag.contains("entry", ignoreCase = true)
    val isRoot = singularity.parentId == null

    if (isRoot && isUnit) {
        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = WorkspaceDesign.SharpShape
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                NodeContent(singularity, onAwaken, children, onPeek)
            }
        }
    } else {
        Column(modifier = modifier.padding(start = 8.dp)) {
            NodeContent(singularity, onAwaken, children, onPeek)
        }
    }
}

@Composable
private fun NodeContent(
    singularity: Singularity,
    onAwaken: (Singularity) -> Unit,
    providedChildren: List<Singularity>? = null,
    onPeek: ((Singularity) -> Unit)? = null
) {
    val repository = EngineCore.provideRepository()

    // Resolve node if it has a targetId
    val resolved by produceState(initialValue = singularity, singularity) {
        value = repository.resolve(singularity)
    }

    val isLinked = singularity.targetId != null
    var peekingTarget by remember { mutableStateOf<Singularity?>(null) }

    val children by if (providedChildren != null) {
        remember(providedChildren) { mutableStateOf(providedChildren) }
    } else {
        repository.getChildrenOf(
            gsId = resolved.gamesystemId,
            fId = resolved.factionId,
            parentId = resolved.id
        ).collectAsState(initial = emptyList())
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = resolved.name,
                style = if (singularity.parentId == null) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
                color = if (singularity.parentId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            if (isLinked) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = "Linked Reference",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable { 
                            onPeek?.invoke(resolved) ?: run { peekingTarget = resolved }
                        }
                )
            }
        }

        resolved.value?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (singularity.xmlTag.contains("selection", ignoreCase = true) || singularity.xmlTag.contains("entry", ignoreCase = true)) {
            IconButton(onClick = { onAwaken(resolved) }) {
                Icon(Icons.Default.Add, contentDescription = "Awaken")
            }
        }
    }

    val stats = children.filter { it.xmlTag.contains("characteristic", ignoreCase = true) }
    val others = children.filter { !it.xmlTag.contains("characteristic", ignoreCase = true) }

    if (stats.isNotEmpty()) {
        StatGrid(stats = stats)
    }

    others.forEach { child ->
        NodeRenderer(
            singularity = child,
            onAwaken = onAwaken,
            onPeek = onPeek,
            modifier = Modifier.padding(top = 4.dp)
        )
    }

    peekingTarget?.let { target ->
        ReferencePreviewDialog(
            singularity = target,
            onDismiss = { peekingTarget = null }
        )
    }
}

@Composable
fun StatGrid(stats: List<Singularity>) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = WorkspaceDesign.SharpShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            stats.forEach { stat ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stat.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stat.value ?: "-",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
