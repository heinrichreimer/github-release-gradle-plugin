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

import com.github.breadmoirai.configuration.GithubReleaseConfiguration
import com.github.breadmoirai.exception.IllegalNetworkResponseCodeException
import com.github.breadmoirai.exception.RepositoryNotFoundException
import com.j256.simplemagic.ContentInfoUtil
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import okhttp3.*
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Provider

class GithubRelease(
        private val owner: Provider<CharSequence>,
        private val repo: Provider<CharSequence>,
        authorization: Provider<CharSequence>,
        private val tagName: Provider<CharSequence>,
        private val targetCommitish: Provider<CharSequence>,
        private val releaseName: Provider<CharSequence>,
        private val body: Provider<CharSequence>,
        private val draft: Provider<Boolean>,
        private val prerelease: Provider<Boolean>,
        private val releaseAssets: FileCollection,
        private val overwrite: Provider<Boolean>,
        private val allowUploadToExisting: Provider<Boolean>
) : Runnable {

    private val authorization = authorization.map { "Token $it" as CharSequence }

    constructor(configuration: GithubReleaseConfiguration) : this(
            owner = configuration.ownerProvider,
            repo = configuration.repoProvider,
            authorization = configuration.authorizationProvider,
            tagName = configuration.tagNameProvider,
            targetCommitish = configuration.targetCommitishProvider,
            releaseName = configuration.releaseNameProvider,
            body = configuration.bodyProvider,
            draft = configuration.draftProvider,
            prerelease = configuration.prereleaseProvider,
            releaseAssets = configuration.releaseAssets,
            overwrite = configuration.overwriteProvider,
            allowUploadToExisting = configuration.allowUploadToExistingProvider
    )

    companion object {
        private val log: Logger = Logging.getLogger(GithubRelease::class.java)
        val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")!!

        internal fun createRequestWithHeaders(authorization: Provider<CharSequence>): Request.Builder {
            return Request.Builder()
                    .addHeader("Authorization", authorization.get().toString())
                    .addHeader("User-Agent", "breadmoirai github-release-gradle-plugin")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .addHeader("Content-Type", JSON.toString())
        }
    }

    private val client: OkHttpClient = OkHttpClient()
    private val slurper: JsonSlurper = JsonSlurper()

    override fun run() {
        val previousReleaseResponse = checkForPreviousRelease()
        val code = previousReleaseResponse.code()
        when (code) {
            200 -> {
                log.info("Found existing release.")
                val ovr = this.overwrite.get()
                when {
                    ovr -> {
                        log.info("Deleting existing release.")
                        deletePreviousRelease(previousReleaseResponse)
                        val createReleaseResponse = createRelease()
                        uploadAssets(createReleaseResponse)
                    }
                    allowUploadToExisting.getOrElse(false) -> {
                        log.info("Assets will be added to existing release.")
                        uploadAssets(previousReleaseResponse)
                    }
                    else -> throw IllegalStateException("Failed to upload release, release already exists.\n" +
                            "Set property 'overwrite = true' to replace existing releases on conflict.")
                }
            }
            404 -> {
                val createReleaseResponse = createRelease()
                uploadAssets(createReleaseResponse)
            }
            else -> throw IllegalNetworkResponseCodeException(previousReleaseResponse)
        }
    }


    private fun checkForPreviousRelease(): Response {
        val releaseUrl = "https://api.github.com/repos/$owner/$repo/releases/tags/$tagName"
        log.debug("Checking for previuos release.")
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

        log.info("Deleting previous release.")
        val request: Request = createRequestWithHeaders(authorization)
                .url(prevReleaseUrl)
                .delete()
                .build()
        val response: Response = client.newCall(request).execute()
        val status = response.code()
        return when (status) {
            204 -> response
            404 -> throw RepositoryNotFoundException(owner.toString(), repo.toString())
            else -> throw IllegalNetworkResponseCodeException(response)
        }
    }

    private fun createRelease(): Response {
        log.info("Creating GitHub release.")
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
        val code = response.code()
        return when (code) {
            201 -> {
                log.info("Created release. Status: ${response.header("Status")}")
                response
            }
            404 -> throw RepositoryNotFoundException(owner.toString(), repo.toString())
            else -> throw IllegalNetworkResponseCodeException(response)
        }
    }

    /**
     * The responses returned is automatically closed for convenience. This behavior may change in the future if required.
     * @param response this response should reference the release that the assets will be uploaded to
     * @return a list of responses from uploaded each asset
     */
    private fun uploadAssets(response: Response): List<Response> {
        if (releaseAssets.isEmpty) {
            log.debug("Skip uploading release assets, no assets found.")
            return emptyList()
        }

        log.info("Uploading release assets.")
        val responseJson = slurper.parseText(response.body()?.string()) as Map<String, Any>

        val contentInfoUtil = ContentInfoUtil()
        val assetResponses = releaseAssets.files.asSequence().map { asset ->
            log.debug("Uploading asset '${asset.name}'")
            val type = contentInfoUtil
                    .findMatch(asset)
                    ?.let { info ->
                        MediaType.parse(info.mimeType)
                    }
            if (type == null) {
                log.warn("Could not guess media type for file '${asset.name}'")
            }
            val uploadUrl: String = responseJson["upload_url"] as String
            val assetBody: RequestBody = RequestBody.create(type, asset)

            val assetPost: Request = createRequestWithHeaders(authorization)
                    .url(uploadUrl.replace("{?name,label}", "?name=${asset.name}"))
                    .post(assetBody)
                    .build()

            client.newCall(assetPost).execute()
        }
        assetResponses.forEach { it.close() }
        return assetResponses.toList()
    }

}
