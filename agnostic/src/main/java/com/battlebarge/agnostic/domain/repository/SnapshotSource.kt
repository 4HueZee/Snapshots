package com.battlebarge.agnostic.domain.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.withTransaction
import com.battlebarge.agnostic.data.local.CategoryLinkEntity
import com.battlebarge.agnostic.data.local.CharacteristicEntity
import com.battlebarge.agnostic.data.local.CostEntity
import com.battlebarge.agnostic.data.local.EdenDatabase
import com.battlebarge.agnostic.data.local.FactionEntity
import com.battlebarge.agnostic.data.local.GameSystemEntity
import com.battlebarge.agnostic.data.local.SingularityEntity
import com.battlebarge.agnostic.data.local.SingularityTagEntity
import com.battlebarge.agnostic.data.local.UnitDatasheetEntity
import com.battlebarge.agnostic.data.remote.DiscoveredGame
import com.battlebarge.agnostic.data.remote.DiscoveredSource
import com.battlebarge.agnostic.data.remote.DiscoveryService
import com.battlebarge.agnostic.data.remote.RuleDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Method 1: Pre-built SQLite Snapshots.
 * Downloads a pre-flattened database from GitHub for instant ingestion using Room withTransaction.
 */
class SnapshotSource(
    private val context: Context,
    private val discoveryService: DiscoveryService,
    private val downloader: RuleDownloader,
    private val database: EdenDatabase
) : RuleSource {
    private val dao = database.singularityDao()

    private val PROXY_OWNER = "4HueZee"
    private val PROXY_REPO = "Snapshots"

    override suspend fun getLibrary(): List<DiscoveredGame> = discoveryService.discoverLibrary()

    override suspend fun getManifest(owner: String, repo: String, branch: String?): List<DiscoveredSource> {
        return discoveryService.discoverSources(owner, repo, branch = branch, quickScan = true)
    }

    override suspend fun transcribe(source: DiscoveredSource, gsId: String): Result<Unit> {
        return transcribeSystem(source.owner, gsId, source.branch)
    }

    override suspend fun transcribeSystem(owner: String, repo: String, branch: String?): Result<Unit> = withContext(Dispatchers.IO) {
        Log.d("SnapshotSource", "Initiating Snapshot Sync: $repo")
        
        val candidateNames = listOf(
            repo,
            repo.lowercase(),
            "wh40k",
            repo.replace("-10th", ""),
            repo.replace("-10th", "-10e"),
            repo.replace("-10e", "-10th"),
            repo.replace("warhammer-", "")
        ).distinct()

        var downloadedFile: File? = null
        var lastException: Exception? = null

        for (candidate in candidateNames) {
            val snapshotUrl = "https://$PROXY_OWNER.github.io/$PROXY_REPO/$candidate.db"
            val tempFile = File(context.cacheDir, "$candidate.db")
            Log.d("SnapshotSource", "Attempting snapshot candidate: $snapshotUrl")

            try {
                downloader.downloadStream(snapshotUrl).use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }

                if (tempFile.length() > 1024) {
                    // Check if file is a valid SQLite database header ("SQLite format 3")
                    val header = ByteArray(16)
                    tempFile.inputStream().use { it.read(header) }
                    val headerString = String(header, Charsets.US_ASCII)
                    if (headerString.startsWith("SQLite format 3")) {
                        downloadedFile = tempFile
                        Log.d("SnapshotSource", "Successfully verified snapshot: $snapshotUrl")
                        break
                    }
                }
                tempFile.delete()
            } catch (e: Exception) {
                tempFile.delete()
                lastException = e
            }
        }

        val tempFile = downloadedFile ?: return@withContext Result.failure(
            lastException ?: Exception("No valid snapshot database found for $repo")
        )

        try {
            database.withTransaction {
                val snapshotDb = SQLiteDatabase.openDatabase(tempFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
                
                try {
                    // 1. Fetch Singularities
                    val cursor = snapshotDb.query("singularities", null, null, null, null, null, null)
                    val categoryIdx = cursor.getColumnIndex("category")
                    val entryTypeIdx = cursor.getColumnIndex("entry_type")
                    val linkTypeIdx = cursor.getColumnIndex("link_type")
                    val valueIdx = cursor.getColumnIndex("value")
                    val parentIdIdx = cursor.getColumnIndex("parent_id")
                    val targetIdIdx = cursor.getColumnIndex("target_id")
                    val entities = mutableListOf<SingularityEntity>()
                    while (cursor.moveToNext()) {
                        entities.add(
                            SingularityEntity(
                                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                                gamesystemId = repo, // Force canonical repo ID
                                factionId = cursor.getString(cursor.getColumnIndexOrThrow("faction_id")),
                                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                                xmlTag = cursor.getString(cursor.getColumnIndexOrThrow("xml_tag")),
                                entryType = if (entryTypeIdx >= 0 && !cursor.isNull(entryTypeIdx)) cursor.getString(entryTypeIdx) else null,
                                value = if (valueIdx >= 0 && !cursor.isNull(valueIdx)) cursor.getString(valueIdx) else null,
                                parentId = if (parentIdIdx >= 0 && !cursor.isNull(parentIdIdx)) cursor.getString(parentIdIdx) else null,
                                targetId = if (targetIdIdx >= 0 && !cursor.isNull(targetIdIdx)) cursor.getString(targetIdIdx) else null,
                                linkType = if (linkTypeIdx >= 0 && !cursor.isNull(linkTypeIdx)) cursor.getString(linkTypeIdx) else null,
                                category = if (categoryIdx >= 0 && !cursor.isNull(categoryIdx)) cursor.getString(categoryIdx) else null,
                                gamesystemName = cursor.getString(cursor.getColumnIndexOrThrow("gamesystem_name")),
                                factionName = cursor.getString(cursor.getColumnIndexOrThrow("faction_name")),
                                isAwakened = cursor.getInt(cursor.getColumnIndexOrThrow("is_awakened")) == 1
                            )
                        )
                        
                        if (entities.size >= 500) {
                            dao.insertSingularitiesIgnore(entities)
                            dao.updateSingularities(entities)
                            entities.clear()
                        }
                    }
                    if (entities.isNotEmpty()) {
                        dao.insertSingularitiesIgnore(entities)
                        dao.updateSingularities(entities)
                    }
                    cursor.close()

                    // 2. Fetch Tags
                    val tagCursor = snapshotDb.query("singularity_tags", null, null, null, null, null, null)
                    val tags = mutableListOf<SingularityTagEntity>()
                    while (tagCursor.moveToNext()) {
                        tags.add(
                            SingularityTagEntity(
                                gamesystemId = repo,
                                factionId = tagCursor.getString(tagCursor.getColumnIndexOrThrow("faction_id")),
                                singularityId = tagCursor.getString(tagCursor.getColumnIndexOrThrow("singularity_id")),
                                tag = tagCursor.getString(tagCursor.getColumnIndexOrThrow("tag"))
                            )
                        )
                        
                        if (tags.size >= 500) {
                            dao.insertTagsIgnore(tags)
                            dao.updateTags(tags)
                            tags.clear()
                        }
                    }
                    if (tags.isNotEmpty()) {
                        dao.insertTagsIgnore(tags)
                        dao.updateTags(tags)
                    }
                    tagCursor.close()

                    // 3. Fetch Category Links if present
                    try {
                        val linkCursor = snapshotDb.query("category_links", null, null, null, null, null, null)
                        val links = mutableListOf<CategoryLinkEntity>()
                        while (linkCursor.moveToNext()) {
                            links.add(
                                CategoryLinkEntity(
                                    gamesystemId = repo,
                                    factionId = linkCursor.getString(linkCursor.getColumnIndexOrThrow("faction_id")),
                                    singularityId = linkCursor.getString(linkCursor.getColumnIndexOrThrow("singularity_id")),
                                    targetId = linkCursor.getString(linkCursor.getColumnIndexOrThrow("target_id")),
                                    categoryName = linkCursor.getString(linkCursor.getColumnIndexOrThrow("category_name")),
                                    isPrimary = linkCursor.getInt(linkCursor.getColumnIndexOrThrow("is_primary")) == 1
                                )
                            )
                            if (links.size >= 500) {
                                dao.insertCategoryLinksIgnore(links)
                                links.clear()
                            }
                        }
                        if (links.isNotEmpty()) dao.insertCategoryLinksIgnore(links)
                        linkCursor.close()
                    } catch (_: Exception) {}

                    // 4. Fetch Costs if present
                    try {
                        val costCursor = snapshotDb.query("costs", null, null, null, null, null, null)
                        val costs = mutableListOf<CostEntity>()
                        while (costCursor.moveToNext()) {
                            costs.add(
                                CostEntity(
                                    gamesystemId = repo,
                                    factionId = costCursor.getString(costCursor.getColumnIndexOrThrow("faction_id")),
                                    singularityId = costCursor.getString(costCursor.getColumnIndexOrThrow("singularity_id")),
                                    name = costCursor.getString(costCursor.getColumnIndexOrThrow("name")),
                                    value = costCursor.getDouble(costCursor.getColumnIndexOrThrow("value"))
                                )
                            )
                            if (costs.size >= 500) {
                                dao.insertCostsIgnore(costs)
                                costs.clear()
                            }
                        }
                        if (costs.isNotEmpty()) dao.insertCostsIgnore(costs)
                        costCursor.close()
                    } catch (_: Exception) {}

                    // 5. Fetch Characteristics if present
                    try {
                        val charCursor = snapshotDb.query("characteristics", null, null, null, null, null, null)
                        val chars = mutableListOf<CharacteristicEntity>()
                        while (charCursor.moveToNext()) {
                            chars.add(
                                CharacteristicEntity(
                                    gamesystemId = repo,
                                    factionId = charCursor.getString(charCursor.getColumnIndexOrThrow("faction_id")),
                                    singularityId = charCursor.getString(charCursor.getColumnIndexOrThrow("singularity_id")),
                                    profileName = charCursor.getString(charCursor.getColumnIndexOrThrow("profile_name")),
                                    profileType = charCursor.getString(charCursor.getColumnIndexOrThrow("profile_type")),
                                    statName = charCursor.getString(charCursor.getColumnIndexOrThrow("stat_name")),
                                    statValue = charCursor.getString(charCursor.getColumnIndexOrThrow("stat_value"))
                                )
                            )
                            if (chars.size >= 500) {
                                dao.insertCharacteristicsIgnore(chars)
                                chars.clear()
                            }
                        }
                        if (chars.isNotEmpty()) dao.insertCharacteristicsIgnore(chars)
                        charCursor.close()
                    } catch (_: Exception) {}
                    // 6. Populate EngineDao Tables for Strongly-Typed Ingestion
                    try {
                        val engineDao = database.engineDao()
                        engineDao.insertGameSystem(
                            GameSystemEntity(
                                id = repo,
                                name = repo,
                                owner = PROXY_OWNER,
                                isInstalled = true
                            )
                        )

                        val factions = entities.map { it.factionId }.distinct().map { fId ->
                            FactionEntity(
                                id = fId,
                                name = fId,
                                gamesystemId = repo,
                                gamesystemName = repo
                            )
                        }
                        if (factions.isNotEmpty()) engineDao.insertFactions(factions)

                        val datasheets = entities.filter { it.xmlTag == "selectionEntry" || it.xmlTag == "entryLink" }.map { e ->
                            UnitDatasheetEntity(
                                id = e.id,
                                gamesystemId = repo,
                                factionId = e.factionId,
                                name = e.name,
                                category = e.category ?: "UNITS",
                                basePoints = e.value?.toIntOrNull() ?: 85
                            )
                        }
                        if (datasheets.isNotEmpty()) engineDao.insertDatasheets(datasheets)
                    } catch (ex: Exception) {
                        Log.w("SnapshotSource", "EngineDao population warning: ${ex.message}")
                    }
                } finally {
                    snapshotDb.close()
                }
            }

            Log.d("SnapshotSource", "Transactional snapshot ingestion complete for $repo")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SnapshotSource", "Failed to ingest snapshot for $repo", e)
            Result.failure(e)
        } finally {
            tempFile.delete()
        }
    }
}
