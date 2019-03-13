/*
 * MIT License
 *
 * Copyright (c) 2019 Jan Heinrich Reimer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/*
 * Based on github-release-gradle-plugin by Ton Ly (@BreadMoirai)
 * Licensed under the Apache License v2.0:
 * https://github.com/BreadMoirai/github-release-gradle-plugin
 */

package com.heinrichreimer.gradle.plugin.github.release

import com.heinrichreimer.gradle.plugin.github.release.api.GitHubApiService
import com.heinrichreimer.gradle.plugin.github.release.api.model.Release
import com.heinrichreimer.gradle.plugin.github.release.api.model.ReleaseInput
import com.heinrichreimer.gradle.plugin.github.release.configuration.GitHubReleaseConfiguration
import com.heinrichreimer.gradle.plugin.github.release.configuration.UpdateMode
import com.heinrichreimer.gradle.plugin.github.release.exception.IllegalNetworkResponseCodeException
import com.heinrichreimer.gradle.plugin.github.release.exception.RepositoryNotFoundException
import com.j256.simplemagic.ContentInfoUtil
import kotlinx.coroutines.awaitAll
import okhttp3.MediaType
import okhttp3.RequestBody
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import retrofit2.Response

class GitHubRelease(configuration: GitHubReleaseConfiguration) : GitHubReleaseConfiguration by configuration {

    companion object {
        private val log: Logger = Logging.getLogger(GitHubRelease::class.java)
    }

    private val service = GitHubApiService(authorizationProvider)

    suspend fun run() {
        val previousReleaseResponse = checkForPreviousRelease()
        val code = previousReleaseResponse.code()
        when (code) {
            200 -> {
                log.info("Found existing release.")
                when (updateMode) {
                    UpdateMode.RELEASE -> {
                        log.info("Deleting existing release.")
                        deletePreviousRelease(previousReleaseResponse)
                        val createReleaseResponse = createRelease()
                        uploadAssets(createReleaseResponse)
                    }
                    UpdateMode.ASSETS -> {
                        log.info("Assets will be added to existing release.")
                        uploadAssets(previousReleaseResponse)
                    }
                    UpdateMode.NONE -> {
                        throw IllegalStateException("Failed to upload release, release already exists.\n" +
                                "Set 'updateMode' property to replace existing releases on conflict.")
                    }
                }
            }
            404 -> {
                val createReleaseResponse = createRelease()
                uploadAssets(createReleaseResponse)
            }
            else -> throw IllegalNetworkResponseCodeException(previousReleaseResponse)
        }
    }


    private suspend fun checkForPreviousRelease(): Response<Release> {
        log.debug("Checking for previuos release.")
        return service.getReleaseByTagNameAsync(owner, repository, tag).await()
    }

    private suspend fun deletePreviousRelease(previous: Response<Release>) {
        val body = previous.body() ?: return

        log.info("Deleting previous release.")
        val response = service
                .deleteReleaseAsync(owner, repository, body.id)
                .await()

        val status = response.code()
        return when (status) {
            204 -> Unit
            404 -> throw RepositoryNotFoundException(owner, repository)
            else -> throw IllegalNetworkResponseCodeException(response)
        }
    }

    private suspend fun createRelease(): Response<Release> {
        log.info("Creating GitHub release.")
        val release = ReleaseInput(
                tag,
                target,
                title,
                body,
                isDraft,
                isPreRelease
        )

        val response: Response<Release> = service
                .createReleaseAsync(owner, repository, release)
                .await()
        val code = response.code()
        return when (code) {
            201 -> {
                log.info("Created release. Status: ${response.headers()["Status"]}")
                response
            }
            404 -> throw RepositoryNotFoundException(owner, repository)
            else -> throw IllegalNetworkResponseCodeException(response)
        }
    }

    /**
     * The responses returned is automatically closed for convenience. This behavior may change in the future if required.
     * @param response this response should reference the release that the assets will be uploaded to
     * @return a list of responses from uploaded each asset
     */
    private suspend fun uploadAssets(response: Response<Release>): List<Response<Release.Asset>> {
        val assets = releaseAssets.files
        if (assets.isEmpty()) {
            log.debug("Skip uploading release assets, no assets found.")
            return emptyList()
        }

        val body = response.body() ?: return emptyList()

        log.info("Uploading release assets.")
        val contentInfoUtil = ContentInfoUtil()
        return assets
                .map { asset ->
                    log.debug("Uploading asset '${asset.name}'")
                    val type = contentInfoUtil
                            .findMatch(asset)
                            ?.let { info ->
                                MediaType.parse(info.mimeType)
                            }
                    if (type == null) {
                        log.warn("Could not guess media type for file '${asset.name}'")
                    }
                    val assetBody: RequestBody = RequestBody.create(type, asset)

                    service.uploadReleaseAssetAsync(body.uploadUrl, assetBody, asset.name)
                }
                .awaitAll()
    }

}
