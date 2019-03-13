package com.github.breadmoirai.api

import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface GitHubService {

    @GET("repos/{owner}/{repo}/releases")
    fun getReleases(
            @Path("owner") owner: String,
            @Path("repo") repo: String
    ): Deferred<Response<List<Release>>>

    @GET("repos/{owner}/{repo}/releases/tags/{tagName}")
    fun getReleaseByTagName(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Path("tagName") tagName: String
    ): Deferred<Response<Release>>

    @GET("repos/{owner}/{repo}/releases/latest")
    fun getLastRelease(
            @Path("owner") owner: String,
            @Path("repo") repo: String
    ): Deferred<Response<Release>>

    @POST("repos/{owner}/{repo}/releases")
    fun createRelease(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Body release: ReleaseInput
    ): Deferred<Response<Release>>

    @DELETE("repos/{owner}/{repo}/releases/{release_id}")
    fun deleteRelease(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Path("release_id") releaseId: Int
    ): Deferred<Response<ResponseBody>>

    @POST
    fun uploadReleaseAsset(
            @Url uploadUrl: HttpUrl,
            @Body release: RequestBody,
            @Query("name") name: String,
            @Query("label") label: String? = null
    ): Deferred<Response<Release.Asset>>

    @GET("repos/{owner}/{repo}/git/refs/tags/{tagName}")
    fun getGitReferenceByTagName(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Path("tagName") tagName: String
    ): Deferred<Response<GitReference>>
}
