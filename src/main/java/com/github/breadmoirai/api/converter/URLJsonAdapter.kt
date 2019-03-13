package com.github.breadmoirai.api.converter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import java.net.URL

object URLJsonAdapter {
    @FromJson
    fun String.eventFromJson(): URL = URL(this)

    @ToJson
    fun URL.eventToJson(): String = toString()
}