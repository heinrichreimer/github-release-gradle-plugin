package com.github.breadmoirai.api

import com.github.breadmoirai.api.converter.URLJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GitHubApiService(
        retrofit: Retrofit
) : GitHubService by retrofit.create(GitHubService::class.java) {

    constructor(
            baseUrl: String = DEFAULT_BASE_URL,
            moshi: Moshi
    ) : this(
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
    )

    constructor(
            baseUrl: String = DEFAULT_BASE_URL
    ) : this(
            baseUrl,
            Moshi.Builder()
                    .add(URLJsonAdapter)
                    .add(KotlinJsonAdapterFactory())
                    .build()
    )

    companion object {
        const val DEFAULT_BASE_URL = "https://api.github.com/"
    }
}