package com.battlebarge.agnostic.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Handles persistence of engine-wide settings, including GitHub authentication.
 */
interface AgnosticSettingsRepository {
    val githubToken: Flow<String?>
    suspend fun updateGithubToken(token: String?)
}
