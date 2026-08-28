package com.battlebarge.agnostic.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.io.IOException

/**
 * Handles streaming raw rule files (.cat, .gst) from the GitHub Raw CDN.
 * Uses OkHttp for efficient, low-memory streaming.
 */
class RuleDownloader(private val okHttpClient: OkHttpClient) {

    /**
     * Downloads a file from a URL and provides an [InputStream].
     * Note: The caller is responsible for closing the stream.
     *
     * @param url The GitHub Raw CDN URL (raw.githubusercontent.com/...).
     * @return An [InputStream] of the file content.
     * @throws IOException If the download fails or the server returns an error.
     */
    fun downloadStream(url: String): InputStream {
        val request = Request.Builder()
            .url(url)
            .build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            response.close()
            throw IOException("Failed to download file from $url. Code: ${response.code}")
        }

        return response.body?.byteStream() ?: throw IOException("Empty response body from $url")
    }
}
