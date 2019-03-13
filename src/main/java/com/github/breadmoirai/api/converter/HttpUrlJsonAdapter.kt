package com.github.breadmoirai.api.converter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import okhttp3.HttpUrl

object HttpUrlJsonAdapter {
    @FromJson
    fun String.eventFromJson(): HttpUrl = HttpUrl.get(this.replace("{?name,label}", "?name&label"))

    @ToJson
    fun HttpUrl.eventToJson(): String = toString()
}