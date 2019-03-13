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

package com.github.breadmoirai

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import java.io.File

interface GithubReleaseConfiguration {

    @get:Input
    var ownerProvider: Provider<CharSequence>
    @get:Input
    var owner: CharSequence

    fun owner(owner: CharSequence) {
        this.owner = owner
    }

    fun owner(owner: Provider<CharSequence>) {
        ownerProvider = owner
    }

    @get:Input
    var repoProvider: Provider<CharSequence>
    @get:Input
    var repo: CharSequence

    fun repo(repo: CharSequence) {
        this.repo = repo
    }

    fun repo(repo: Provider<CharSequence>) {
        repoProvider = repo
    }

    @get:Input
    var authorizationProvider: Provider<CharSequence>
    @get:Input
    var authorization: CharSequence

    fun authorization(authorization: CharSequence) {
        this.authorization = authorization
    }

    fun authorization(authorization: Provider<CharSequence>) {
        authorizationProvider = authorization
    }

    @get:Input
    var tokenProvider: Provider<CharSequence>
        get() = authorizationProvider
        set(value) {
            authorizationProvider = value
        }
    @get:Input
    var token: CharSequence
        get() = authorization
        set(value) {
            authorization = value
        }

    fun token(token: CharSequence) = authorization(token)
    fun token(token: Provider<CharSequence>) = authorization(token)

    @get:Input
    var tagNameProvider: Provider<CharSequence>
    @get:Input
    var tagName: CharSequence

    fun tagName(tagName: CharSequence) {
        this.tagName = tagName
    }

    fun tagName(tagName: Provider<CharSequence>) {
        tagNameProvider = tagName
    }

    @get:Input
    var targetCommitishProvider: Provider<CharSequence>
    @get:Input
    var targetCommitish: CharSequence

    fun targetCommitish(targetCommitish: CharSequence) {
        this.targetCommitish = targetCommitish
    }

    fun targetCommitish(targetCommitish: Provider<CharSequence>) {
        targetCommitishProvider = targetCommitish
    }

    @get:Input
    var releaseNameProvider: Provider<CharSequence>
    @get:Input
    var releaseName: CharSequence

    fun releaseName(releaseName: CharSequence) {
        this.releaseName = releaseName
    }

    fun releaseName(releaseName: Provider<CharSequence>) {
        releaseNameProvider = releaseName
    }

    @get:Input
    var bodyProvider: Provider<CharSequence>
    @get:Input
    var body: CharSequence

    fun body(body: CharSequence) {
        this.body = body
    }

    fun body(body: Provider<CharSequence>) {
        bodyProvider = body
    }

    @get:Input
    var draftProvider: Provider<Boolean>
    @get:Input
    var draft: Boolean

    fun draft(draft: Boolean) {
        this.draft = draft
    }

    fun draft(draft: Provider<Boolean>) {
        draftProvider = draft
    }

    @get:Input
    var prereleaseProvider: Provider<Boolean>
    @get:Input
    var prerelease: Boolean

    fun prerelease(prerelease: Boolean) {
        this.prerelease = prerelease
    }

    fun prerelease(prerelease: Provider<Boolean>) {
        prereleaseProvider = prerelease
    }

    @get:InputFiles
    var releaseAssetsCollection: FileCollection
    @get:InputFiles
    var releaseAssets: Iterable<File>
    @get:InputFiles
    var releaseAsset: File

    fun releaseAssets(assets: Iterable<Any>)
    fun releaseAssets(vararg assets: Any) = releaseAsset(assets.asIterable())
    fun releaseAsset(asset: Any)

    @get:Input
    var overwriteProvider: Provider<Boolean>
    @get:Input
    var overwrite: Boolean

    fun overwrite(overwrite: Boolean) {
        this.overwrite = overwrite
    }

    fun overwrite(overwrite: Provider<Boolean>) {
        overwriteProvider = overwrite
    }

    @get:Input
    var allowUploadToExistingProvider: Provider<Boolean>
    @get:Input
    var allowUploadToExisting: Boolean

    fun allowUploadToExisting(allowUploadToExisting: Boolean) {
        this.allowUploadToExisting = allowUploadToExisting
    }

    fun allowUploadToExisting(allowUploadToExisting: Provider<Boolean>) {
        allowUploadToExistingProvider = allowUploadToExisting
    }

}
