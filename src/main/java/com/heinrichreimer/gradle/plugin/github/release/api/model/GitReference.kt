package com.heinrichreimer.gradle.plugin.github.release.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitReference(
        @Json(name = "object")
        val referencedObject: Object
) {
    data class Object(
            @Json(name = "sha")
            val sha: String
    )
}