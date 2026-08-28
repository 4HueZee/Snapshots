package com.battlebarge.agnostic.data.parser

import android.util.Xml
import com.battlebarge.agnostic.data.local.SingularityEntity
import com.battlebarge.agnostic.data.local.SingularityTagEntity
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

/**
 * Result of a single parse operation, containing the entities and their tags.
 */
data class SingularityParseResult(
    val entities: List<SingularityEntity>,
    val tags: List<SingularityTagEntity>
)

/**
 * A streaming XML parser that converts rule files into Singularities.
 * High-performance and low-memory because it doesn't load the entire tree into RAM.
 */
class SingularityParser {

    /**
     * Parses the [InputStream] into a list of [SingularityEntity] and [SingularityTagEntity].
     *
     * @param inputStream The XML stream to parse.
     */
    fun parse(inputStream: InputStream): SingularityParseResult {
        val entities = mutableListOf<SingularityEntity>()
        val tags = mutableListOf<SingularityTagEntity>()
        
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)

        var eventType = parser.eventType
        val idStack = mutableListOf<String>()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val tagName = parser.name
                    val id = parser.getAttributeValue(null, "id")
                    val name = parser.getAttributeValue(null, "name")
                    
                    if (id != null && name != null) {
                        val parentId = if (idStack.isNotEmpty()) idStack.last() else null
                        
                        entities.add(
                            SingularityEntity(
                                id = id,
                                name = name,
                                type = tagName,
                                value = parser.getAttributeValue(null, "value"),
                                parentId = parentId
                            )
                        )
                        
                        // Example tag extraction: If the XML tag itself is a classification
                        tags.add(SingularityTagEntity(id, tagName))
                        
                        idStack.add(id)
                    }
                }
                XmlPullParser.END_TAG -> {
                    // Pop from the stack only if the tag had an ID (meaning we pushed it)
                    // Note: This logic is simplified; a robust parser needs to track tag names
                    // for more complex nested rules.
                    if (parser.getAttributeValue(null, "id") != null) {
                        if (idStack.isNotEmpty()) idStack.removeAt(idStack.size - 1)
                    }
                }
            }
            eventType = parser.next()
        }

        return SingularityParseResult(entities, tags)
    }
}
