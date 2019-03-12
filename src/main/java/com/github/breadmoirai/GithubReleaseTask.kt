/*
 *    Copyright 2017 - 2018 BreadMoirai (Ton Ly)
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

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.util.concurrent.Callable

class GithubReleaseTask : DefaultTask() {

    init {
        group = "publishing"
    }

    @Input val owner : Property<CharSequence> = project.objects.property(CharSequence::class.java)
    @Input val repo : Property<CharSequence> = project.objects.property(CharSequence::class.java)
    @Input val authorization : Property<CharSequence> = project.objects.property(CharSequence::class.java)
    @Input val tagName : Property<CharSequence> = project.objects.property(CharSequence::class.java)
    @Input val targetCommitish : Property<CharSequence> = project.objects.property(CharSequence::class.java)
    @Input val releaseName : Property<CharSequence> = project.objects.property(CharSequence::class.java)
    @Input val body : Property<CharSequence> = project.objects.property(CharSequence::class.java)
    @Input val draft : Property<Boolean> = project.objects.property(Boolean::class.java)
    @Input val prerelease : Property<Boolean> = project.objects.property(Boolean::class.java)
    @InputFiles val releaseAssets : ConfigurableFileCollection = project.files()
    @Input val overwrite : Property<Boolean> = project.objects.property(Boolean::class.java)
    @Input val allowUploadToExisting : Property<Boolean> = project.objects.property(Boolean::class.java)

    @TaskAction
    fun publishRelease() {
        val tag = tagName.getOrNull()
                ?: throw PropertyNotSetException("tagName")
        val tar = targetCommitish.getOrNull()
                ?: throw PropertyNotSetException("targetCommitish")
        val rel = releaseName.getOrNull()
                ?: throw PropertyNotSetException("releaseName")
        val bod = body.getOrNull()
                ?: throw PropertyNotSetException("body")
        val own = owner.getOrNull()
                ?: throw PropertyNotSetException("owner")
        val rep = repo.getOrNull()
                ?: throw PropertyNotSetException("repo")
        val dra = draft.getOrNull()
                ?: throw PropertyNotSetException("draft")
        val pre = prerelease.getOrNull()
                ?: throw PropertyNotSetException("prerelease")
        val auth = authorization.getOrNull()
                ?: throw PropertyNotSetException("authorization")
        val releaseAssets = releaseAssets
        GithubRelease(
                own,
                rep,
                auth,
                tag,
                tar,
                rel,
                bod,
                dra,
                pre,
                releaseAssets,
                overwrite,
                allowUploadToExisting
        ).run()
    }

    fun setOwner(owner : CharSequence) = this.owner.set(owner)

    fun setOwner(owner : Provider<CharSequence>) = this.owner.set(owner)

    fun setOwner(owner : Callable<CharSequence>) = this.owner.set(project.provider(owner))

    fun setRepo(repo : CharSequence) = this.repo.set(repo)

    fun setRepo(repo : Provider<CharSequence>) = this.repo.set(repo)

    fun setRepo(repo : Callable<CharSequence>) = this.repo.set(project.provider(repo))

    fun setToken(token : CharSequence) = this.authorization.set("Token $token")

    fun setToken(token : Provider<CharSequence>) = this.authorization.set(token.map { "Token $it" })

    fun setToken(token : Callable<CharSequence>) = this.authorization.set(project.provider(token).map { "Token $it" })

    fun setAuthorization(authorization : CharSequence) = this.authorization.set(authorization)

    fun setAuthorization(authorization : Provider<CharSequence>) = this.authorization.set(authorization)

    fun setAuthorization(authorization : Callable<CharSequence>) =
            this.authorization.set(project.provider(authorization))

    fun setTagName(tagName : CharSequence) = this.tagName.set(tagName)

    fun setTagName(tagName : Provider<CharSequence>) = this.tagName.set(tagName)

    fun setTagName(tagName : Callable<CharSequence>) = this.tagName.set(project.provider(tagName))

    fun setTargetCommitish(targetCommitish : CharSequence) = this.targetCommitish.set(targetCommitish)

    fun setTargetCommitish(targetCommitish : Provider<CharSequence>) = this.targetCommitish.set(targetCommitish)

    fun setTargetCommitish(targetCommitish : Callable<CharSequence>) =
            this.targetCommitish.set(project.provider(targetCommitish))

    fun setReleaseName(releaseName : CharSequence) = this.releaseName.set(releaseName)

    fun setReleaseName(releaseName : Provider<CharSequence>) = this.releaseName.set(releaseName)

    fun setReleaseName(releaseName : Callable<CharSequence>) = this.releaseName.set(project.provider(releaseName))

    fun setBody(body : CharSequence) = this.body.set(body)

    fun setBody(body : Provider<CharSequence>) = this.body.set(body)

    fun setBody(body : Callable<CharSequence>) = this.body.set(project.provider(body))

    fun setDraft(draft : Boolean) = this.draft.set(draft)

    fun setDraft(draft : Provider<Boolean>) = this.draft.set(draft)

    fun setDraft(draft : Callable<Boolean>) = this.draft.set(project.provider(draft))

    fun setPrerelease(prerelease : Boolean) = this.prerelease.set(prerelease)

    fun setPrerelease(prerelease : Provider<Boolean>) = this.prerelease.set(prerelease)

    fun setPrerelease(prerelease : Callable<Boolean>) = this.prerelease.set(project.provider(prerelease))

    fun setReleaseAssets(vararg assets: Any) = this.releaseAssets.setFrom(assets)

    fun setOverwrite(overwrite : Boolean) = this.overwrite.set(overwrite)

    fun setOverwrite(overwrite : Provider<Boolean>) = this.overwrite.set(overwrite)

    fun setOverwrite(overwrite : Callable<Boolean>) = this.overwrite.set(project.provider(overwrite))

    fun setAllowUploadToExisting(allowUploadToExisting : Boolean) =
            this.allowUploadToExisting.set(allowUploadToExisting)

    fun setAllowUploadToExisting(allowUploadToExisting : Provider<Boolean>) =
            this.allowUploadToExisting.set(allowUploadToExisting)

    fun setAllowUploadToExisting(allowUploadToExisting : Callable<Boolean>) =
            this.allowUploadToExisting.set(project.provider(allowUploadToExisting))

}
