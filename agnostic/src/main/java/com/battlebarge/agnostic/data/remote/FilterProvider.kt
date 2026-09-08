package com.battlebarge.agnostic.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Data representing the parsed "Smart Manifest" from the Beacon Gist.
 */
data class FilterManifest(
    val org: String = "BSData",
    val includeKeywords: List<String> = emptyList(),
    val excludeKeywords: List<String> = emptyList(),
    val refreshIntervalHours: Int = 24
)

/**
 * Fetches and parses the "Smart Manifest" from a remote Gist to control app discovery.
 */
class FilterProvider(
    private val okHttpClient: OkHttpClient,
    private val manifestUrl: String = "https://gist.githubusercontent.com/4HueZee/45b3c8d6491e1713d1bfd8e2436ea561/raw/f96f1ea3e49dd4f68955b2cde1aa74a5a46eb618/BattleBarge_scout.txt"
) {
    private var cachedManifest: FilterManifest? = null
    private var lastFetchTime: Long = 0

    // The "Safe List" fallback required by the plan
    private val safeManifest = FilterManifest(
        org = "BSData",
        includeKeywords = listOf("40k", "kill-team", "horus-heresy"),
        excludeKeywords = listOf("archive", "test", "template"),
        refreshIntervalHours = 24
    )

    /**
     * Fetches the manifest from the Gist URL.
     * Implements a 24-hour throttle to prevent GitHub token exhaustion.
     * Falls back to internal [safeManifest] if the fetch fails.
     */
    suspend fun fetchManifest(): FilterManifest = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val cacheDuration = (cachedManifest?.refreshIntervalHours ?: 24) * 60 * 60 * 1000L
        
        if (cachedManifest != null && (now - lastFetchTime) < cacheDuration) {
            return@withContext cachedManifest!!
        }

        try {
            val request = Request.Builder().url(manifestUrl).build()
            val response = okHttpClient.newCall(request).execute()
            
            if (!response.isSuccessful) return@withContext cachedManifest ?: safeManifest
            
            val content = response.body?.string() ?: return@withContext cachedManifest ?: safeManifest
            val parsed = parseManifest(content)
            
            // Safety Check: Reject the manifest if it lacks basic structure
            val finalManifest = if (parsed.org.isBlank() || (parsed.includeKeywords.isEmpty() && parsed.excludeKeywords.isEmpty())) {
                cachedManifest ?: safeManifest
            } else {
                parsed
            }

            cachedManifest = finalManifest
            lastFetchTime = now
            finalManifest
        } catch (e: Exception) {
            cachedManifest ?: safeManifest
        }
    }

    /**
     * Internal logic to parse the manifest text format.
     */
    private fun parseManifest(content: String): FilterManifest {
        var org = "BSData"
        val includes = mutableListOf<String>()
        val excludes = mutableListOf<String>()
        var refresh = 24

        content.lines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isEmpty() || trimmed.startsWith("#")) return@forEach

            when {
                trimmed.startsWith("org:", ignoreCase = true) -> {
                    org = trimmed.substringAfter(":").trim()
                }
                trimmed.startsWith("refresh:", ignoreCase = true) -> {
                    val value = trimmed.substringAfter(":").trim()
                    // Extract digits (handles "24h")
                    refresh = value.filter { it.isDigit() }.toIntOrNull() ?: 24
                }
                trimmed.startsWith("-") -> {
                    val keyword = trimmed.substring(1).trim().lowercase()
                    if (keyword.isNotEmpty()) excludes.add(keyword)
                }
                else -> {
                    // Handle explicit '+' or just the keyword
                    val keyword = if (trimmed.startsWith("+")) trimmed.substring(1).trim() else trimmed
                    if (keyword.isNotEmpty()) {
                        includes.add(keyword.lowercase())
                    }
                }
            }
        }

        return FilterManifest(org, includes, excludes, refresh)
    }
}
