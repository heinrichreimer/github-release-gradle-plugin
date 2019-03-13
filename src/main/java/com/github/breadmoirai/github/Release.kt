package com.github.breadmoirai.github

import java.net.URL

data class Release(
        val url: URL,
        val upload_url: URL,
        val tag_name: String,
        val target_commitish: String,
        val name: String,
        val body: String,
        val draft: Boolean,
        val prerelease: Boolean
)