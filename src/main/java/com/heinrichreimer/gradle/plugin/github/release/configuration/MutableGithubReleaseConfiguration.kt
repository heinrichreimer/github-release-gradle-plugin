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

interface MutableGithubReleaseConfiguration : GithubReleaseConfiguration {

    override var ownerProvider: Provider<String>
    override var owner: String

    fun owner(owner: String) {
        this.owner = owner
    }

    fun owner(owner: Provider<String>) {
        ownerProvider = owner
    }

    fun owner(owner: () -> String)

    override var repositoryProvider: Provider<String>
    override var repository: String

    fun repository(repository: String) {
        this.repository = repository
    }

    fun repository(repository: Provider<String>) {
        repositoryProvider = repository
    }

    fun repository(repository: () -> String)

    override var authorizationProvider: Provider<String>
    override var authorization: String

    fun authorization(authorization: String) {
        this.authorization = authorization
    }

    fun authorization(authorization: Provider<String>) {
        authorizationProvider = authorization
    }

    fun authorization(authorization: () -> String)

    override var tokenProvider: Provider<String>
        get() = authorizationProvider
        set(value) {
            authorizationProvider = value
        }
    override var token: String
        get() = authorization
        set(value) {
            authorization = value
        }

    fun token(token: String) = authorization(token)
    fun token(token: Provider<String>) = authorization(token)
    fun token(token: () -> String) = authorization(token)

    override var tagProvider: Provider<String>
    override var tag: String

    fun tag(tag: String) {
        this.tag = tag
    }

    fun tag(tag: Provider<String>) {
        tagProvider = tag
    }

    fun tag(tag: () -> String)

    override var targetProvider: Provider<String>
    override var target: String

    fun target(target: String) {
        this.target = target
    }

    fun target(target: Provider<String>) {
        targetProvider = target
    }

    fun target(target: () -> String)

    override var nameProvider: Provider<String>
    override var name: String

    fun name(name: String) {
        this.name = name
    }

    fun name(name: Provider<String>) {
        nameProvider = name
    }

    fun name(name: () -> String)

    override var bodyProvider: Provider<String>
    override var body: String

    fun body(body: String) {
        this.body = body
    }

    fun body(body: Provider<String>) {
        bodyProvider = body
    }

    fun body(body: () -> String)

    override var isDraftProvider: Provider<Boolean>
    override var isDraft: Boolean

    fun isDraft(isDraft: Boolean) {
        this.isDraft = isDraft
    }

    fun isDraft(isDraft: Provider<Boolean>) {
        isDraftProvider = isDraft
    }

    fun isDraft(isDraft: () -> Boolean)

    override var isPreReleaseProvider: Provider<Boolean>
    override var isPreRelease: Boolean

    fun isPreRelease(isPreRelease: Boolean) {
        this.isPreRelease = isPreRelease
    }

    fun isPreRelease(isPreRelease: Provider<Boolean>) {
        isPreReleaseProvider = isPreRelease
    }

    fun isPreRelease(isPreRelease: () -> Boolean)

    override var releaseAssets: FileCollection

    /**
     * @param releaseAssetPaths The release asset paths, evaluated as per [org.gradle.api.Project.files].
     */
    fun releaseAssets(releaseAssetPaths: Iterable<*>)

    /**
     * @param releaseAssetPaths The release asset paths, evaluated as per [org.gradle.api.Project.files].
     */
    fun releaseAssets(vararg releaseAssetPaths: Any)

    override var updateModeProvider: Provider<UpdateMode>
    override var updateMode: UpdateMode

    fun updateMode(updateMode: UpdateMode) {
        this.updateMode = updateMode
    }

    fun updateMode(updateMode: Provider<UpdateMode>) {
        updateModeProvider = updateMode
    }

    fun updateMode(updateMode: () -> UpdateMode)


}
