package com.battlebarge.agnostic.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Data class representing a GitHub repository.
 */
data class GithubRepositoryResponse(
    val name: String,
    val description: String?,
    val default_branch: String,
    val html_url: String
)

/**
 * Data class representing a file entry in a GitHub repository directory listing.
 */
data class GithubDirectoryEntry(
    val name: String,
    val path: String,
    val sha: String,
    val type: String, // "file" or "dir"
    val download_url: String?
)

/**
 * Data class representing the relevant part of the GitHub Content API response for a single file.
 */
data class GithubContentResponse(
    val sha: String,
    val name: String,
    val path: String
)

/**
 * Data class representing the GitHub Trees API response.
 */
data class GithubTreeResponse(
    val sha: String,
    val url: String,
    val tree: List<GithubTreeEntry>,
    val truncated: Boolean
)

/**
 * Data class representing an entry in a GitHub Tree.
 */
data class GithubTreeEntry(
    val path: String,
    val mode: String,
    val type: String, // "blob" (file) or "tree" (directory)
    val size: Long?,
    val sha: String,
    val url: String
)

/**
 * Retrofit service for interacting with the GitHub API.
 */
interface GithubApiService {

    /**
     * Lists all repositories for an organization.
     */
    @GET("orgs/{org}/repos")
    suspend fun getOrgRepos(
        @Path("org") org: String,
        @Header("Authorization") auth: String? = null,
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): Response<List<GithubRepositoryResponse>>

    /**
     * Fetches metadata for a specific repository.
     */
    @GET("repos/{owner}/{repo}")
    suspend fun getRepoMetadata(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("Authorization") auth: String? = null
    ): Response<GithubRepositoryResponse>

    /**
     * Fetches metadata for a specific file or directory.
     */
    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Query("ref") ref: String? = null,
        @Header("Authorization") auth: String? = null
    ): Response<List<GithubDirectoryEntry>>

    /**
     * Fetches metadata for a specific file. (Simplified for single-file SHA checks)
     */
    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getFileMetadata(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Query("ref") ref: String? = null,
        @Header("Authorization") auth: String? = null
    ): Response<GithubContentResponse>

    /**
     * Fetches the full Git Tree for a repository.
     * Use recursive=1 to get the entire structure.
     */
    @GET("repos/{owner}/{repo}/git/trees/{tree_sha}")
    suspend fun getTree(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("tree_sha") treeSha: String,
        @Query("recursive") recursive: Int = 1,
        @Header("Authorization") auth: String? = null
    ): Response<GithubTreeResponse>
}
