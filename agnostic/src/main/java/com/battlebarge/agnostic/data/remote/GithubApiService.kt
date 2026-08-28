package com.battlebarge.agnostic.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Data class representing the relevant part of the GitHub Content API response.
 */
data class GithubContentResponse(
    val sha: String,
    val name: String,
    val path: String
)

/**
 * Retrofit service for interacting with the GitHub API.
 * Used primarily to check the SHA (hash) of rule files to avoid redundant downloads.
 */
interface GithubApiService {

    /**
     * Fetches metadata for a specific file in a GitHub repository.
     *
     * @param owner The repository owner (e.g., "bsdata").
     * @param repo The repository name (e.g., "wh40k-10th-edition").
     * @param path The path to the file within the repository.
     */
    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getFileMetadata(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): Response<GithubContentResponse>
}
