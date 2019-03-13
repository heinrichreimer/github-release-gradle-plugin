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
