package com.battlebarge.agnostic.presentation

import com.battlebarge.agnostic.data.local.DownloadedFaction
import com.battlebarge.agnostic.data.local.DownloadedGameSystem
import com.battlebarge.agnostic.data.local.RosterEntity
import com.battlebarge.agnostic.data.remote.DiscoveredGame
import com.battlebarge.agnostic.data.remote.DiscoveredSource
import com.battlebarge.agnostic.domain.model.Argonaut
import com.battlebarge.agnostic.domain.model.Singularity

// Active Sub-Hub View Contexts (Single-Page Command Hub)
enum class ActiveSubHub {
    GAMES,
    LISTS,
    MODELS
}

/**
 * Functional status for an installed game engine.
 */
enum class EngineUpdateStatus {
    UP_TO_DATE,
    OUTDATED,
    SYNCING,
    CACHE_ERROR,
    NEEDS_SYNC
}

/**
 * Unified State Interface for the Agnostic Workspace.
 */
sealed interface WorkspaceUiState {
    data object Loading : WorkspaceUiState

    data class Active(
        val activeSubHub: ActiveSubHub = ActiveSubHub.GAMES,
        val installedGames: List<DownloadedGameSystem> = emptyList(),
        val gameStatuses: Map<String, EngineUpdateStatus> = emptyMap(),
        val allAvailableGames: List<DiscoveredGame> = emptyList(),
        val selectedGameForInfo: DownloadedGameSystem? = null,
        val gameCatalogues: List<DownloadedFaction> = emptyList(),
        val isAddGamesDialogOpen: Boolean = false,
        val isLibraryLoading: Boolean = false,
        val isLibraryFetching: Boolean = false,
        val downloadingGameIds: Set<String> = emptySet(),
        val isUpdatingAll: Boolean = false,
        val armyLists: List<RosterEntity> = emptyList(),
        
        // Active Roster Inspection
        val inspectingRoster: RosterEntity? = null,
        val activeRosterArgonauts: List<Argonaut> = emptyList(),

        // In-Window Unit Selection Sheet
        val isUnitSelectionSheetOpen: Boolean = false,
        val unitSelectionCategory: String? = null,
        val availableUnitsForSelection: List<Singularity> = emptyList(),

        // Army List Wizard
        val isNewListWizardOpen: Boolean = false,
        val selectedSystemForNewList: DownloadedGameSystem? = null,
        val availableFactionsForNewList: List<DiscoveredSource> = emptyList(),
        val isFactionsLoading: Boolean = false,

        // Settings Toggles
        val useHighSpeedSnapshots: Boolean = false
    ) : WorkspaceUiState

    /**
     * Viewport for browsing technical rules and unit stats.
     */
    data class RuleBrowser(
        val gamesystemId: String,
        val factionId: String,
        val sourceRosterId: String? = null // Optional context if viewing from a list
    ) : WorkspaceUiState

    data class Error(val message: String) : WorkspaceUiState
}
