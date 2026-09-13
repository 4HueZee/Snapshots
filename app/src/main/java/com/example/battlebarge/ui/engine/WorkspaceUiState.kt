package com.example.battlebarge.ui.engine

import com.battlebarge.agnostic.data.remote.DiscoveredGame
import com.battlebarge.agnostic.data.remote.DiscoveredSource
import com.battlebarge.agnostic.domain.model.Singularity

/**
 * Sealed interface defining all possible UI states for the Agnostic Workspace.
 * This prevents "contradiction errors" by ensuring the screen only shows one valid state at a time.
 */
sealed interface WorkspaceUiState {
    /**
     * The app is scanning for Game Systems (the "Scout" phase).
     */
    data object Scanning : WorkspaceUiState

    /**
     * The user is browsing the library of discovered game systems.
     */
    data class Library(val games: List<DiscoveredGame>) : WorkspaceUiState

    /**
     * The user has selected a game system and is "peeking" at available factions.
     */
    data class FactionDiscovery(
        val selectedGame: DiscoveredGame,
        val sources: List<DiscoveredSource>,
        val isLoading: Boolean = false
    ) : WorkspaceUiState

    /**
     * A faction is being downloaded and transcribed into Eden.
     */
    data class Syncing(
        val message: String,
        val queueSize: Int = 1
    ) : WorkspaceUiState

    /**
     * Rules are loaded and the user is viewing the Codex.
     */
    data class Codex(
        val gamesystemId: String,
        val factionId: String,
        val rootNodes: List<Singularity> = emptyList()
    ) : WorkspaceUiState

    /**
     * Something went wrong (e.g., 404, No Internet).
     */
    data class Error(val message: String) : WorkspaceUiState
}

enum class WorkspaceViewMode { CODEX, ROSTER }
