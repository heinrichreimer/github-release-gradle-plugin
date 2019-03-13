package com.github.breadmoirai.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseInput(
        val tag_name: String,
        val target_commitish: String,
        val name: String,
        val body: String,
        val draft: Boolean,
        val prerelease: Boolean
)