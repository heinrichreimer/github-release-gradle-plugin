package com.heinrichreimer.gradle.plugin.github.release.api

import com.squareup.moshi.JsonClass
import okhttp3.HttpUrl

@JsonClass(generateAdapter = true)
data class Release(
        val id: Int,
        val url: HttpUrl,
        val upload_url: HttpUrl,
        val tag_name: String,
        val target_commitish: String,
        val name: String,
        val body: String,
        val draft: Boolean,
        val prerelease: Boolean
) {
    data class Asset(
            val name: String,
            val state: State
    ) {
        enum class State {
            uploaded
        }
    }
}