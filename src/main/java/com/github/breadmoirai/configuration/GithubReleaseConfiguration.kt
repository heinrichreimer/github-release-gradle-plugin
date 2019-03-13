/*
 *    Copyright 2017 - 2018 BreadMoirai (Ton Ly), 2018 Jan Heinrich Reimer
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

package com.github.breadmoirai.configuration

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles

interface GithubReleaseConfiguration {

    @get:Input
    val ownerProvider: Provider<CharSequence>
    @get:Input
    val owner: CharSequence

    @get:Input
    val repoProvider: Provider<CharSequence>
    @get:Input
    val repo: CharSequence

    @get:Input
    val authorizationProvider: Provider<CharSequence>
    @get:Input
    val authorization: CharSequence

    @get:Input
    val tokenProvider: Provider<CharSequence>
        get() = authorizationProvider
    @get:Input
    val token: CharSequence
        get() = authorization

    @get:Input
    val tagNameProvider: Provider<CharSequence>
    @get:Input
    val tagName: CharSequence

    @get:Input
    val targetCommitishProvider: Provider<CharSequence>
    @get:Input
    val targetCommitish: CharSequence

    @get:Input
    val releaseNameProvider: Provider<CharSequence>
    @get:Input
    val releaseName: CharSequence

    @get:Input
    val bodyProvider: Provider<CharSequence>
    @get:Input
    val body: CharSequence

    @get:Input
    val draftProvider: Provider<Boolean>
    @get:Input
    val draft: Boolean

    @get:Input
    val prereleaseProvider: Provider<Boolean>
    @get:Input
    val prerelease: Boolean

    @get:InputFiles
    val releaseAssets: FileCollection

    @get:Input
    val overwriteProvider: Provider<Boolean>
    @get:Input
    val overwrite: Boolean

    @get:Input
    val allowUploadToExistingProvider: Provider<Boolean>
    @get:Input
    val allowUploadToExisting: Boolean

}
