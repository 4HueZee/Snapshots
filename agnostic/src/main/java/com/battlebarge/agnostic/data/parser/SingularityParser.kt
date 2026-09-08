package com.battlebarge.agnostic.data.parser

import android.util.Xml
import com.battlebarge.agnostic.data.local.SingularityEntity
import com.battlebarge.agnostic.data.local.SingularityTagEntity
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

/**
 * A streaming XML parser that converts rule files into Singularities.
 * Uses relative interpretation of Battlescribe XML primary <categoryLink> specifications.
 */
class SingularityParser {

    private fun mapUniversalCategory(rawName: String?): String? {
        if (rawName.isNullOrBlank()) return null
        val u = rawName.uppercase().trim()
        return when {
            u == "HQ" || u.contains("CHARACTER") || u.contains("HERO") || u.contains("LEADER") || u.contains("WARLORD") || u.contains("COMMAND") -> "CHARACTERS"
            u == "TROOPS" || u.contains("BATTLELINE") || u.contains("CORE") -> "BATTLELINE"
            u.contains("TRANSPORT") -> "DEDICATED TRANSPORTS"
            else -> rawName.trim()
        }
    }

    /**
     * Parses the [InputStream] and calls [onBatchReady] with chunks of entities.
     * Extracts 'targetId', 'type' (linkType), and primary '<categoryLink>' for universal category mapping.
     */
    suspend fun parseInBatches(
        inputStream: InputStream,
        gamesystemId: String,
        gamesystemName: String,
        factionId: String,
        factionName: String,
        batchSize: Int = 500,
        onBatchReady: suspend (List<SingularityEntity>, List<SingularityTagEntity>) -> Unit
    ) {
        val currentEntities = mutableListOf<SingularityEntity>()
        val currentTags = mutableListOf<SingularityTagEntity>()
        
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)

        var eventType = parser.eventType
        val idStack = mutableListOf<String>()
        val tagStack = mutableListOf<String>()

        var currentFactionId = factionId
        var currentFactionName = factionName

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val tagName = parser.name
                    val id = parser.getAttributeValue(null, "id")
                    val name = parser.getAttributeValue(null, "name")

                    // Capture exact numerical/GUID faction ID and name from root catalogue or gameSystem tag
                    if ((tagName == "catalogue" || tagName == "gameSystem") && id != null) {
                        currentFactionId = id.trim()
                        if (!name.isNullOrBlank()) {
                            currentFactionName = name.trim()
                        }
                    }
                    
                    if (id != null) {
                        val parentId = idStack.lastOrNull()
                        val targetId = parser.getAttributeValue(null, "targetId")
                        val linkType = parser.getAttributeValue(null, "type")
                        
                        // ID Normalization: trim to ensure matching across sources
                        val normalizedId = id.trim()
                        val normalizedTargetId = targetId?.trim()

                        // Extract category context from name, linkType, or category attributes
                        var category = mapUniversalCategory(name) ?: mapUniversalCategory(linkType)

                        if (tagName == "categoryLink") {
                            val catName = name ?: parser.getAttributeValue(null, "targetId")
                            val isPrimary = parser.getAttributeValue(null, "primary") == "true"
                            val mappedCat = mapUniversalCategory(catName)
                            if (mappedCat != null && (isPrimary || category == null)) {
                                category = mappedCat
                            }
                        }

                        currentEntities.add(
                            SingularityEntity(
                                id = normalizedId,
                                gamesystemId = gamesystemId,
                                gamesystemName = gamesystemName,
                                factionId = currentFactionId,
                                factionName = currentFactionName,
                                name = name ?: tagName,
                                xmlTag = tagName,
                                value = parser.getAttributeValue(null, "value"),
                                parentId = parentId,
                                targetId = normalizedTargetId,
                                linkType = linkType,
                                category = category
                            )
                        )
                        
                        currentTags.add(
                            SingularityTagEntity(
                                gamesystemId = gamesystemId,
                                factionId = currentFactionId,
                                singularityId = normalizedId,
                                tag = tagName
                            )
                        )
                        
                        idStack.add(normalizedId)
                        tagStack.add(tagName)

                        if (currentEntities.size >= batchSize) {
                            onBatchReady(currentEntities.toList(), currentTags.toList())
                            currentEntities.clear()
                            currentTags.clear()
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    val tagName = parser.name
                    if (tagStack.lastOrNull() == tagName) {
                        tagStack.removeAt(tagStack.size - 1)
                        idStack.removeAt(idStack.size - 1)
                    }
                }
            }
            eventType = parser.next()
        }

        if (currentEntities.isNotEmpty()) {
            onBatchReady(currentEntities, currentTags)
        }
    }
}
