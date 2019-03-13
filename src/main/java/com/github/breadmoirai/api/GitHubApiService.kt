package com.github.breadmoirai.api

import com.github.breadmoirai.api.converter.HttpUrlJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.gradle.api.provider.Provider
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GitHubApiService(
        retrofit: Retrofit
) : GitHubService by retrofit.create(GitHubService::class.java) {

    constructor(
            authorization: Provider<CharSequence>,
            baseUrl: String = DEFAULT_BASE_URL

    ) : this(
            Retrofit.Builder()
                    .client(
                            OkHttpClient.Builder()
                                    .addInterceptor { chain ->
                                        chain.proceed(
                                                chain.request()
                                                        .newBuilder()
                                                        .addHeader("Authorization", "token ${authorization.get()}")
                                                        .addHeader("User-Agent", "breadmoirai github-release-gradle-plugin")
                                                        .addHeader("Accept", "application/vnd.github.v3+json")
                                                        .addHeader("Content-Type", JSON.toString())
                                                        .build()
                                        )
                                    }
                                    .build()
                    )
                    .baseUrl(baseUrl)
                    .addConverterFactory(
                            MoshiConverterFactory.create(
                                    Moshi.Builder()
                                            .add(HttpUrlJsonAdapter)
                                            .add(KotlinJsonAdapterFactory())
                                            .build()
                            )
                    )
                    .build()
    )

    companion object {
        private val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")!!
        private const val DEFAULT_BASE_URL = "https://api.github.com/"
    }
}