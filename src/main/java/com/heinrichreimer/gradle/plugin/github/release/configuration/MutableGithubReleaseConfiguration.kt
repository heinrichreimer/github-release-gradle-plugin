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

    override var repoProvider: Provider<String>
    override var repo: String

    fun repo(repo: String) {
        this.repo = repo
    }

    fun repo(repo: Provider<String>) {
        repoProvider = repo
    }

    fun repo(repo: () -> String)

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

    override var tagNameProvider: Provider<String>
    override var tagName: String

    fun tagName(tagName: String) {
        this.tagName = tagName
    }

    fun tagName(tagName: Provider<String>) {
        tagNameProvider = tagName
    }

    fun tagName(tagName: () -> String)

    override var targetCommitishProvider: Provider<String>
    override var targetCommitish: String

    fun targetCommitish(targetCommitish: String) {
        this.targetCommitish = targetCommitish
    }

    fun targetCommitish(targetCommitish: Provider<String>) {
        targetCommitishProvider = targetCommitish
    }

    fun targetCommitish(targetCommitish: () -> String)

    override var releaseNameProvider: Provider<String>
    override var releaseName: String

    fun releaseName(releaseName: String) {
        this.releaseName = releaseName
    }

    fun releaseName(releaseName: Provider<String>) {
        releaseNameProvider = releaseName
    }

    fun releaseName(releaseName: () -> String)

    override var bodyProvider: Provider<String>
    override var body: String

    fun body(body: String) {
        this.body = body
    }

    fun body(body: Provider<String>) {
        bodyProvider = body
    }

    fun body(body: () -> String)

    override var draftProvider: Provider<Boolean>
    override var draft: Boolean

    fun draft(draft: Boolean) {
        this.draft = draft
    }

    fun draft(draft: Provider<Boolean>) {
        draftProvider = draft
    }

    fun draft(draft: () -> Boolean)

    override var prereleaseProvider: Provider<Boolean>
    override var prerelease: Boolean

    fun prerelease(prerelease: Boolean) {
        this.prerelease = prerelease
    }

    fun prerelease(prerelease: Provider<Boolean>) {
        prereleaseProvider = prerelease
    }

    fun prerelease(prerelease: () -> Boolean)

    override var releaseAssets: FileCollection

    fun releaseAssets(releaseAssets: FileCollection) {
        this.releaseAssets = releaseAssets
    }

    override var overwriteProvider: Provider<Boolean>
    override var overwrite: Boolean

    fun overwrite(overwrite: Boolean) {
        this.overwrite = overwrite
    }

    fun overwrite(overwrite: Provider<Boolean>) {
        overwriteProvider = overwrite
    }

    fun overwrite(overwrite: () -> Boolean)

    override var allowUploadToExistingProvider: Provider<Boolean>
    override var allowUploadToExisting: Boolean

    fun allowUploadToExisting(allowUploadToExisting: Boolean) {
        this.allowUploadToExisting = allowUploadToExisting
    }

    fun allowUploadToExisting(allowUploadToExisting: Provider<Boolean>) {
        allowUploadToExistingProvider = allowUploadToExisting
    }

    fun allowUploadToExisting(allowUploadToExisting: () -> Boolean)

}
