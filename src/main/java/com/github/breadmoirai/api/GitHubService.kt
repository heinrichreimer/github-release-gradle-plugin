package com.github.breadmoirai.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubService {

    @GET("repos/:owner/:repo/releases")
    fun getReleases(
            @Path("owner") owner: String,
            @Path("repo") repo: String
    ): Call<List<Release>>

    @GET("repos/:owner/:repo/releases/tags/:tagName")
    fun getReleaseByTagName(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Path("tagName") tagName: String
    ): Call<Release>

    @GET("repos/:owner/:repo/releases/latest")
    fun getLastRelease(
            @Path("owner") owner: String,
            @Path("repo") repo: String
    ): Call<Release>

    @POST("repos/:owner/:repo/releases")
    fun createRelease(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Body release: Release
    ): Call<Release>

    @GET("repos/:owner/:repo/git/refs/tags/:tagName")
    fun getGitReferenceByTagName(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Path("tagName") tagName: String
    ): Call<GitReference>
}
