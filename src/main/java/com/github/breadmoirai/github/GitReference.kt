package com.github.breadmoirai.github

import com.squareup.moshi.Json

data class GitReference(
        @Json(name = "object")
        val referenced_object: Object
) {
    data class Object(
            val sha: String
    )
}