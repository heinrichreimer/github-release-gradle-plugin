package com.heinrichreimer.gradle.plugin.github.release.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitReference(
        @field:Json(name = "object")
        val referenced_object: Object
) {
    data class Object(
            val sha: String
    )
}