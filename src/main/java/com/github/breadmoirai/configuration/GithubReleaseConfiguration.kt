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
