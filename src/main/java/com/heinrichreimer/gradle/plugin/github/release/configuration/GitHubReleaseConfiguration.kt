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

package com.heinrichreimer.gradle.plugin.github.release.configuration

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles

interface GitHubReleaseConfiguration {

    @get:Input
    val ownerProvider: Provider<String>
    @get:Input
    val owner: String
        get() = ownerProvider.get()

    @get:Input
    val repositoryProvider: Provider<String>
    @get:Input
    val repository: String
        get() = repositoryProvider.get()

    @get:Input
    val authorizationProvider: Provider<String>
    @get:Input
    val authorization: String
        get() = authorizationProvider.get()

    @get:Input
    val tokenProvider: Provider<String>
        get() = authorizationProvider
    @get:Input
    val token: String
        get() = authorization

    @get:Input
    val tagProvider: Provider<String>
    @get:Input
    val tag: String
        get() = tagProvider.get()

    @get:Input
    val targetProvider: Provider<String>
    @get:Input
    val target: String
        get() = targetProvider.get()

    @get:Input
    val titleProvider: Provider<String>
    @get:Input
    val title: String
        get() = titleProvider.get()

    @get:Input
    val bodyProvider: Provider<String>
    @get:Input
    val body: String
        get() = bodyProvider.get()

    @get:Input
    val isDraftProvider: Provider<Boolean>
    @get:Input
    val isDraft: Boolean
        get() = isDraftProvider.get()

    @get:Input
    val isPreReleaseProvider: Provider<Boolean>
    @get:Input
    val isPreRelease: Boolean
        get() = isPreReleaseProvider.get()

    @get:InputFiles
    val releaseAssets: FileCollection

    @get:Input
    val updateModeProvider: Provider<UpdateMode>
    @get:Input
    val updateMode: UpdateMode
        get() = updateModeProvider.get()
}
