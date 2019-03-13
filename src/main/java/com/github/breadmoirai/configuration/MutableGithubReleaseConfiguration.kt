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

interface MutableGithubReleaseConfiguration : GithubReleaseConfiguration {

    override var ownerProvider: Provider<CharSequence>
    override var owner: CharSequence

    fun owner(owner: CharSequence) {
        this.owner = owner
    }

    fun owner(owner: Provider<CharSequence>) {
        ownerProvider = owner
    }

    fun owner(owner: () -> CharSequence)

    override var repoProvider: Provider<CharSequence>
    override var repo: CharSequence

    fun repo(repo: CharSequence) {
        this.repo = repo
    }

    fun repo(repo: Provider<CharSequence>) {
        repoProvider = repo
    }

    fun repo(repo: () -> CharSequence)

    override var authorizationProvider: Provider<CharSequence>
    override var authorization: CharSequence

    fun authorization(authorization: CharSequence) {
        this.authorization = authorization
    }

    fun authorization(authorization: Provider<CharSequence>) {
        authorizationProvider = authorization
    }

    fun authorization(authorization: () -> CharSequence)

    override var tokenProvider: Provider<CharSequence>
        get() = authorizationProvider
        set(value) {
            authorizationProvider = value
        }
    override var token: CharSequence
        get() = authorization
        set(value) {
            authorization = value
        }

    fun token(token: CharSequence) = authorization(token)
    fun token(token: Provider<CharSequence>) = authorization(token)
    fun token(token: () -> CharSequence) = authorization(token)

    override var tagNameProvider: Provider<CharSequence>
    override var tagName: CharSequence

    fun tagName(tagName: CharSequence) {
        this.tagName = tagName
    }

    fun tagName(tagName: Provider<CharSequence>) {
        tagNameProvider = tagName
    }

    fun tagName(tagName: () -> CharSequence)

    override var targetCommitishProvider: Provider<CharSequence>
    override var targetCommitish: CharSequence

    fun targetCommitish(targetCommitish: CharSequence) {
        this.targetCommitish = targetCommitish
    }

    fun targetCommitish(targetCommitish: Provider<CharSequence>) {
        targetCommitishProvider = targetCommitish
    }

    fun targetCommitish(targetCommitish: () -> CharSequence)

    override var releaseNameProvider: Provider<CharSequence>
    override var releaseName: CharSequence

    fun releaseName(releaseName: CharSequence) {
        this.releaseName = releaseName
    }

    fun releaseName(releaseName: Provider<CharSequence>) {
        releaseNameProvider = releaseName
    }

    fun releaseName(releaseName: () -> CharSequence)

    override var bodyProvider: Provider<CharSequence>
    override var body: CharSequence

    fun body(body: CharSequence) {
        this.body = body
    }

    fun body(body: Provider<CharSequence>) {
        bodyProvider = body
    }

    fun body(body: () -> CharSequence)

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
