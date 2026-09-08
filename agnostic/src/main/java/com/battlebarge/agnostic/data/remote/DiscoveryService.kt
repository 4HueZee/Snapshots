package com.battlebarge.agnostic.data.remote

import android.util.Xml
import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.local.GameSystemMetadataEntity
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Data representing a discovered game system (GitHub Repository).
 */
data class DiscoveredGame(
    val name: String,
    val description: String?,
    val defaultBranch: String,
    val owner: String,
    val repoName: String
)

/**
 * Data representing a discovered rule file before full transcription.
 */
data class DiscoveredSource(
    val name: String,
    val id: String,
    val gameSystemId: String,
    val fileName: String,
    val type: SourceType,
    val sha: String,
    val downloadUrl: String,
    val owner: String,
    val repo: String,
    val branch: String
)

enum class SourceType { GAME_SYSTEM, CATALOGUE }

/**
 * Service that "Peeks" into remote repositories to identify rulesets.
 */
class DiscoveryService(
    private val apiService: GithubApiService,
    private val downloader: RuleDownloader,
    database: EdenDatabase,
    private val filterProvider: FilterProvider
) {
    private val dao = database.singularityDao()

    companion object {
        val CURATED_LIBRARY = listOf(
            DiscoveredGame("Warhammer 40,000 10th Edition", "wh40k-10th", "main", "BSData", "wh40k-10th"),
            DiscoveredGame("Warhammer 40,000 7th Edition", "wh40k-7th-edition", "main", "BSData", "wh40k-7th-edition"),
            DiscoveredGame("The Horus Heresy 2nd Edition", "horus-heresy-2nd-edition", "main", "BSData", "horus-heresy-2nd-edition"),
            DiscoveredGame("Warhammer Age of Sigmar", "warhammer-aos", "main", "BSData", "warhammer-aos"),
            DiscoveredGame("Warhammer 40,000: Kill Team", "wh40k-killteam", "main", "BSData", "wh40k-killteam"),
            DiscoveredGame("Blood Bowl", "blood-bowl", "main", "BSData", "blood-bowl"),
            DiscoveredGame("Middle-earth Strategy Battle Game", "mesbg", "main", "BSData", "mesbg"),
            DiscoveredGame("OnePageRules: Grimdark Future", "opr-grimdark-future", "main", "BSData", "opr-grimdark-future")
        )
    }

    /**
     * Dynamically discovers all 50+ wargaming repositories from the official BSData Gallery.
     * Zero API rate limits, full community gallery support.
     */
    suspend fun discoverLibrary(): List<DiscoveredGame> = withContext(Dispatchers.IO) {
        try {
            // 1. Fetch Official BSData Gallery JSON (Zero Rate Limits)
            val galleryUrl = "https://raw.githubusercontent.com/BSData/gallery/main/bsdata.catpkg-gallery.json"
            downloader.downloadStream(galleryUrl).use { stream ->
                val text = stream.bufferedReader().use { it.readText() }
                val json = JSONObject(text)
                val reposArray = json.optJSONArray("repositories")
                
                if (reposArray != null) {
                    val games = mutableListOf<DiscoveredGame>()
                    for (i in 0 until reposArray.length()) {
                        val repoObj = reposArray.getJSONObject(i)
                        val name = repoObj.optString("name")
                        val description = repoObj.optString("description")
                        val location = repoObj.optString("location") // e.g. "BSData/wh40k"
                        val repoName = if (location.contains("/")) location.substringAfter("/") else location
                        val owner = if (location.contains("/")) location.substringBefore("/") else "BSData"

                        if (repoName.isNotBlank()) {
                            games.add(
                                DiscoveredGame(
                                    name = if (name.isNotBlank()) name else repoName.replace("-", " ").uppercase(),
                                    description = description,
                                    defaultBranch = "main",
                                    owner = owner,
                                    repoName = repoName
                                )
                            )
                        }
                    }
                    if (games.isNotEmpty()) {
                        return@withContext games.sortedBy { it.name }
                    }
                }
            }
        } catch (_: Exception) {}

        // Fallback Curated Library
        CURATED_LIBRARY
    }

    /**
     * Locates the primary Game System Core (.gst) file in a repository.
     */
    suspend fun findCoreFile(
        owner: String,
        repo: String,
        branch: String? = null
    ): GithubDirectoryEntry? = withContext(Dispatchers.IO) {
        val targetBranch = branch ?: try {
            apiService.getRepoMetadata(owner, repo).body()?.default_branch ?: "main"
        } catch (e: Exception) {
            "main"
        }

        val response = try {
            apiService.getContents(owner, repo, "", targetBranch)
        } catch (e: Exception) {
            return@withContext null
        }

        if (!response.isSuccessful) return@withContext null

        // Find the first .gst file
        response.body()?.find { it.name.endsWith(".gst") }
    }

    /**
     * Lists and identifies all ruleset files in a repository folder.
     * If [quickScan] is true, it identifies .cat files by name/extension without peeking at headers.
     */
    suspend fun discoverSources(
        owner: String,
        repo: String,
        path: String = "",
        branch: String? = null,
        quickScan: Boolean = false
    ): List<DiscoveredSource> = withContext(Dispatchers.IO) {
        // Fetch repo metadata to get default branch if not provided
        val targetBranch = branch ?: try {
            apiService.getRepoMetadata(owner, repo).body()?.default_branch ?: "main"
        } catch (e: Exception) {
            "main"
        }

        val response = try {
            apiService.getContents(owner, repo, path, targetBranch)
        } catch (e: Exception) {
            return@withContext emptyList()
        }

        if (!response.isSuccessful) return@withContext emptyList()

        val entries = response.body() ?: return@withContext emptyList()

        if (quickScan) {
            // Map .cat files directly without downloading headers
            entries.filter { it.name.endsWith(".cat") }.map { entry ->
                DiscoveredSource(
                    name = entry.name.removeSuffix(".cat"), // Temporary name until full transcribe
                    id = entry.sha, // Use SHA as temporary ID for identification
                    gameSystemId = "", // Unknown until transcribed
                    fileName = entry.name,
                    type = SourceType.CATALOGUE,
                    sha = entry.sha,
                    downloadUrl = entry.download_url ?: "",
                    owner = owner,
                    repo = repo,
                    branch = targetBranch
                )
            }
        } else {
            entries.filter { 
                it.name.endsWith(".cat") || it.name.endsWith(".gst") 
            }.mapNotNull { entry ->
                peekMetadata(entry, owner, repo, targetBranch)
            }
        }
    }

    private fun peekMetadata(
        entry: GithubDirectoryEntry,
        owner: String,
        repo: String,
        branch: String
    ): DiscoveredSource? {
        val url = entry.download_url ?: return null
        return try {
            downloader.downloadStream(url).use { stream ->
                val buffered = BufferedInputStream(stream)

                val parser = Xml.newPullParser()
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(buffered, null)

                var eventType = parser.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        val tagName = parser.name
                        if (tagName == "gameSystem" || tagName == "catalogue") {
                            val name = parser.getAttributeValue(null, "name")
                            val id = parser.getAttributeValue(null, "id")
                            val gsId = if (tagName == "gameSystem") id else parser.getAttributeValue(null, "gameSystemId")

                            if (name != null && id != null && gsId != null) {
                                return DiscoveredSource(
                                    name = name,
                                    id = id,
                                    gameSystemId = gsId,
                                    fileName = entry.name,
                                    type = if (tagName == "gameSystem") SourceType.GAME_SYSTEM else SourceType.CATALOGUE,
                                    sha = entry.sha,
                                    downloadUrl = url,
                                    owner = owner,
                                    repo = repo,
                                    branch = branch
                                )
                            }
                        }
                    }
                    eventType = parser.next()
                }
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
