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

package com.github.breadmoirai.configuration

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles

interface GithubReleaseConfiguration {

    @get:Input
    val ownerProvider: Provider<String>
    @get:Input
    val owner: String

    @get:Input
    val repoProvider: Provider<String>
    @get:Input
    val repo: String

    @get:Input
    val authorizationProvider: Provider<String>
    @get:Input
    val authorization: String

    @get:Input
    val tokenProvider: Provider<String>
        get() = authorizationProvider
    @get:Input
    val token: String
        get() = authorization

    @get:Input
    val tagNameProvider: Provider<String>
    @get:Input
    val tagName: String

    @get:Input
    val targetCommitishProvider: Provider<String>
    @get:Input
    val targetCommitish: String

    @get:Input
    val releaseNameProvider: Provider<String>
    @get:Input
    val releaseName: String

    @get:Input
    val bodyProvider: Provider<String>
    @get:Input
    val body: String

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
