package com.heinrichreimer.gradle.plugin.github.release.api

import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface GitHubService {

    @GET("repos/{owner}/{repository}/releases")
    fun getReleases(
            @Path("owner") owner: String,
            @Path("repository") repo: String
    ): Deferred<Response<List<Release>>>

    @GET("repos/{owner}/{repository}/releases/tags/{tag}")
    fun getReleaseByTagName(
            @Path("owner") owner: String,
            @Path("repository") repo: String,
            @Path("tag") tagName: String
    ): Deferred<Response<Release>>

    @GET("repos/{owner}/{repository}/releases/latest")
    fun getLastRelease(
            @Path("owner") owner: String,
            @Path("repository") repo: String
    ): Deferred<Response<Release>>

    @POST("repos/{owner}/{repository}/releases")
    fun createRelease(
            @Path("owner") owner: String,
            @Path("repository") repo: String,
            @Body release: ReleaseInput
    ): Deferred<Response<Release>>

    @DELETE("repos/{owner}/{repository}/releases/{release_id}")
    fun deleteRelease(
            @Path("owner") owner: String,
            @Path("repository") repo: String,
            @Path("release_id") releaseId: Int
    ): Deferred<Response<ResponseBody>>

    @POST
    fun uploadReleaseAsset(
            @Url uploadUrl: HttpUrl,
            @Body release: RequestBody,
            @Query("name") name: String,
            @Query("label") label: String? = null
    ): Deferred<Response<Release.Asset>>

    @GET("repos/{owner}/{repository}/git/refs/tags/{tag}")
    fun getGitReferenceByTagName(
            @Path("owner") owner: String,
            @Path("repository") repo: String,
            @Path("tag") tagName: String
    ): Deferred<Response<GitReference>>
}
