package com.battlebarge.agnostic.domain.repository

import com.battlebarge.agnostic.data.remote.DiscoveredGame
import com.battlebarge.agnostic.data.remote.DiscoveredSource

/**
 * Common technical contract for all rule ingestion methods.
 * Decouples the UI from the data origin (Snapshot vs Streaming).
 */
interface RuleSource {

    /**
     * Identifies available repositories in the community library.
     */
    suspend fun getLibrary(): List<DiscoveredGame>

    /**
     * Lists all rule files (.gst, .cat) within a specific repository.
     */
    suspend fun getManifest(owner: String, repo: String, branch: String? = null): List<DiscoveredSource>

    /**
     * Ingests a specific rule file into the local database.
     * @param gsId The unique ID (Repo Name) for the game system.
     */
    suspend fun transcribe(source: DiscoveredSource, gsId: String): Result<Unit>
    
    /**
     * High-speed bulk ingestion of an entire system.
     * For Snapshots, this is a single file. For Streaming, it processes the tree.
     */
    suspend fun transcribeSystem(owner: String, repo: String, branch: String? = null): Result<Unit>
}
