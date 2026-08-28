package com.battlebarge.agnostic.data.parser

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

/**
 * A lightweight validator that performs a basic syntax check on XML rule files.
 * This acts as a "Gate" to ensure we don't attempt to parse malformed files into Eden.
 */
class RuleValidator {

    /**
     * Checks if the provided [InputStream] contains valid XML syntax.
     * This is a non-exhaustive check (it doesn't validate schema, just well-formedness).
     *
     * @param inputStream The stream to validate.
     * @return True if well-formed, false otherwise.
     */
    fun isWellFormed(inputStream: InputStream): Boolean {
        return try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // We just iterate through the whole document to check for syntax errors
                eventType = parser.next()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
