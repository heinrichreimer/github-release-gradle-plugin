package com.heinrichreimer.gradle.plugin.github.release.api

import com.heinrichreimer.gradle.plugin.github.release.api.model.GitReference
import com.heinrichreimer.gradle.plugin.github.release.api.model.Release
import com.heinrichreimer.gradle.plugin.github.release.api.model.ReleaseInput
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface GitHubService {

    @GET("repos/{owner}/{repository}/releases")
    fun getReleasesAsync(
            @Path("owner") owner: String,
            @Path("repository") repository: String
    ): Deferred<Response<List<Release>>>

    @GET("repos/{owner}/{repository}/releases/tags/{tag}")
    fun getReleaseByTagNameAsync(
            @Path("owner") owner: String,
            @Path("repository") repository: String,
            @Path("tag") tag: String
    ): Deferred<Response<Release>>

    @GET("repos/{owner}/{repository}/releases/latest")
    fun getLastReleaseAsync(
            @Path("owner") owner: String,
            @Path("repository") repository: String
    ): Deferred<Response<Release>>

    @POST("repos/{owner}/{repository}/releases")
    fun createReleaseAsync(
            @Path("owner") owner: String,
            @Path("repository") repository: String,
            @Body release: ReleaseInput
    ): Deferred<Response<Release>>

    @DELETE("repos/{owner}/{repository}/releases/{release_id}")
    fun deleteReleaseAsync(
            @Path("owner") owner: String,
            @Path("repository") repository: String,
            @Path("release_id") releaseId: Int
    ): Deferred<Response<ResponseBody>>

    @POST
    fun uploadReleaseAssetAsync(
            @Url uploadUrl: HttpUrl,
            @Body release: RequestBody,
            @Query("name") name: String,
            @Query("label") label: String? = null
    ): Deferred<Response<Release.Asset>>

    @GET("repos/{owner}/{repository}/git/refs/tags/{tag}")
    fun getGitReferenceByTagNameAsync(
            @Path("owner") owner: String,
            @Path("repository") repository: String,
            @Path("tag") tag: String
    ): Deferred<Response<GitReference>>
}
