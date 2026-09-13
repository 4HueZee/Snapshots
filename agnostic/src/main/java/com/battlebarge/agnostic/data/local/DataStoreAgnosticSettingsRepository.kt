package com.battlebarge.agnostic.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.battlebarge.agnostic.domain.repository.AgnosticSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "agnostic_settings")

/**
 * DataStore implementation of [AgnosticSettingsRepository].
 */
class DataStoreAgnosticSettingsRepository(private val context: Context) : AgnosticSettingsRepository {

    private val GITHUB_TOKEN = stringPreferencesKey("github_token")

    override val githubToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[GITHUB_TOKEN]
    }

    override suspend fun updateGithubToken(token: String?) {
        context.dataStore.edit { preferences ->
            if (token == null) {
                preferences.remove(GITHUB_TOKEN)
            } else {
                preferences[GITHUB_TOKEN] = token
            }
        }
    }
}
