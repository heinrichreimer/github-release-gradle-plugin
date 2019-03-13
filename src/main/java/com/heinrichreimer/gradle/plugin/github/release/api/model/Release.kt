package com.heinrichreimer.gradle.plugin.github.release.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.HttpUrl

@JsonClass(generateAdapter = true)
data class Release(
        @Json(name = "id")
        val id: Int,
        @Json(name = "url")
        val url: HttpUrl,
        @Json(name = "upload_url")
        val uploadUrl: HttpUrl,
        @Json(name = "tag_name")
        val tag: String,
        @Json(name = "target_commitish")
        val target: String,
        @Json(name = "name")
        val title: String,
        @Json(name = "body")
        val body: String,
        @Json(name = "draft")
        val isDraft: Boolean,
        @Json(name = "prerelease")
        val isPreRelease: Boolean
) {
    @JsonClass(generateAdapter = true)
    data class Asset(
            @Json(name = "title")
            val name: String,
            @Json(name = "state")
            val state: State
    ) {
        @JsonClass(generateAdapter = true)
        enum class State {
            @Json(name = "uploaded")
            UPLOADED
        }
    }
}