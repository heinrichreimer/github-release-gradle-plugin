package com.heinrichreimer.gradle.plugin.github.release.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseInput(
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
)