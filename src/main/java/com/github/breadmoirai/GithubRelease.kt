/*
 *    Copyright 2017 - 2018 BreadMoirai (Ton Ly)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.breadmoirai

import com.j256.simplemagic.ContentInfo
import com.j256.simplemagic.ContentInfoUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import okhttp3.*
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Provider

class GithubRelease(
        private val owner: CharSequence,
        private val repo: CharSequence,
        private val authorization: CharSequence,
        private val tagName: CharSequence,
        private val targetCommitish: CharSequence,
        private val releaseName: CharSequence,
        private val body: CharSequence,
        private val draft: Boolean,
        private val prerelease: Boolean,
        private val releaseAssets: FileCollection,
        private val overwrite: Provider<Boolean>,
        private val allowUploadToExisting: Provider<Boolean>
) : Runnable {

    companion object {
        private val logger: Logger = Logging.getLogger(GithubRelease::class.java)
        val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")!!

        internal fun createRequestWithHeaders(authorization: CharSequence): Request.Builder {
            return Request.Builder()
                    .addHeader("Authorization", authorization.toString())
                    .addHeader("User-Agent", "breadmoirai github-release-gradle-plugin")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .addHeader("Content-Type", "application/json")
        }
    }

    private val client: OkHttpClient = OkHttpClient()
    private val slurper: JsonSlurper = JsonSlurper()

    override fun run() {
        val previousReleaseResponse = checkForPreviousRelease()
        val code = previousReleaseResponse.code()
        when (code) {
            200 -> {
                println(":githubRelease EXISTING RELEASE FOUND")
                val ovr = this.overwrite.getOrNull()
                        ?: throw PropertyNotSetException("overwrite")
                when {
                    ovr -> {
                        logger.info(":githubRelease EXISTING RELEASE DELETED")
                        deletePreviousRelease(previousReleaseResponse)
                        val createReleaseResponse = createRelease()
                        uploadAssets(createReleaseResponse)
                    }
                    allowUploadToExisting.getOrElse(false) -> {
                        logger.info(":githubRelease Assets will added to existing release")
                        uploadAssets(previousReleaseResponse)
                    }
                    else -> {
                        val s = ":githubRelease FAILED RELEASE ALREADY EXISTS\n\tSet property['overwrite'] to true to replace existing releases"
                        logger.error(s)
                        throw Error(s)
                    }
                }
            }
            404 -> {
                val createReleaseResponse = createRelease()
                uploadAssets(createReleaseResponse)
            }
            else -> {
                val s = ":githubRelease FAILED ERROR CODE $code"
                logger.error(s)
                throw Error("$s\n${previousReleaseResponse.body()?.string()}")
            }
        }
    }


    private fun checkForPreviousRelease(): Response {
        val releaseUrl = "https://api.github.com/repos/$owner/$repo/releases/tags/$tagName"
        println(":githubRelease CHECKING FOR PREVIOUS RELEASE $releaseUrl")
        val request: Request = createRequestWithHeaders(authorization)
                .url(releaseUrl)
                .get()
                .build()
        return client
                .newCall(request)
                .execute()
    }

    private fun deletePreviousRelease(previous: Response): Response {
        val responseJson = slurper.parseText(previous.body()?.string()) as Map<String, Any>
        val prevReleaseUrl: String = responseJson["url"] as String

        println(":githubRelease DELETING PREVIOUS RELEASE $prevReleaseUrl")
        val request: Request = createRequestWithHeaders(authorization)
                .url(prevReleaseUrl)
                .delete()
                .build()
        val response: Response = client.newCall(request).execute()
        val status = response.code()
        if (status != 204) {
            if (status == 404) {
                throw  Error("404 Repository with Owner: '$owner' and Name: '$repo' was not found")
            }
            throw  Error("Couldn't delete old release: $status\n$response")
        }
        return response
    }

    private fun createRelease(): Response {
        println(":githubRelease CREATING RELEASE")
        val json: String = JsonOutput.toJson(mapOf(
                "tag_name" to tagName,
                "target_commitish" to targetCommitish,
                "name" to releaseName,
                "body" to body,
                "draft" to draft,
                "prerelease" to prerelease
        ))
        val requestBody: RequestBody = RequestBody.create(JSON, json)
        val request: Request = createRequestWithHeaders(authorization)
                .url("https://api.github.com/repos/$owner/$repo/releases")
                .post(requestBody)
                .build()

        val response: Response = client.newCall(request).execute()
        val status = response.code()
        if (status != 201) {
            val body = response.body()?.string()
            if (status == 404) {
                throw  Error("404 Repository with Owner: '$owner' and Name: '$repo' was not found")
            }
            throw  Error("Could not create release: $status ${response.message()}\n$body")
        }
        println(":githubRelease STATUS ${response.header("Status")?.toUpperCase()}")
        return response
    }

    /**
     * The responses returned is automatically closed for convenience. This behavior may change in the future if required.
     * @param response this response should reference the release that the assets will be uploaded to
     * @return a list of responses from uploaded each asset
     */
    private fun uploadAssets(response: Response): List<Response> {
        println(":githubRelease UPLOADING ASSETS")
        val responseJson = slurper.parseText(response.body()?.string()) as Map<String, Any>

        val util = ContentInfoUtil()
        if (releaseAssets.isEmpty()) {
            println(":githubRelease NO ASSETS FOUND")
            return emptyList()
        }
        val assetResponses = releaseAssets.files.asSequence().map { asset ->
            println(":githubRelease UPLOADING $asset.name")
            val info: ContentInfo = util.findMatch(asset) ?: ContentInfo.EMPTY_INFO
            val type: MediaType? = MediaType.parse(info.mimeType)
            if (type == null)
                println(":githubRelease WARNING Mime Type could not be determined")
            val uploadUrl: String = responseJson["upload_url"] as String
            val assetBody: RequestBody = RequestBody.create(type, asset)

            val assetPost: Request = createRequestWithHeaders(authorization)
                    .url(uploadUrl.replace("{?name,label}", "?name=$asset.name"))
                    .post(assetBody)
                    .build()

            client.newCall(assetPost).execute()
        }
        assetResponses.forEach { it.close() }
        return assetResponses.toList()
    }

}
