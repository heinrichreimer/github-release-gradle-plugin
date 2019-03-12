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

import groovy.lang.Closure
import groovy.lang.DelegatesTo
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

import java.util.concurrent.Callable

/**
 * An extension for the [GithubReleasePlugin]
 * <p> See Default Values below </p>
 * <p>
 *     <table>
 *         <tr>
 *             <th>Property</th>
 *             <th>Default Value</th>
 *         </tr>
 *         <tr>
 *             <td>owner</td>
 *             <td>project.group<br />
 *                 part after last period</td>
 *         </tr>
 *         <tr>
 *             <td>repo</td>
 *             <td>project.name ||<br />
 *                 rootProject.name</td>
 *         </tr>
 *         <tr>
 *             <td>tagName</td>
 *             <td>'v' + project.version</td>
 *         </tr>
 *         <tr>
 *             <td>targetCommitish</td>
 *             <td>'master'</td>
 *         </tr>
 *         <tr>
 *             <td>releaseName</td>
 *             <td>'v' + project.version</td>
 *         </tr>
 *         <tr>
 *             <td>body</td>
 *             <td>list of commits since last release</td>
 *         </tr>
 *         <tr>
 *             <td>draft</td>
 *             <td>false</td>
 *         </tr>
 *         <tr>
 *             <td>prerelease</td>
 *             <td>false</td>
 *         </tr>
 *         <tr>
 *             <td>authorization</td>
 *             <td>N/A</td>
 *         </tr>
 *         <tr>
 *             <td>overwrite</td>
 *             <td>false</td>
 *         </tr>
 *         <tr>
 *              <td>allowUploadToExisting</td>
 *              <td>false</td>
 *          </tr>
 *     </table>
 * </p>
 *
 */
class GithubReleaseExtension(private val project: Project) {

    val owner: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    val repo: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    val authorization: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    val tagName: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    val targetCommitish: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    val releaseName: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    val body: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    val draft: Property<Boolean> = project.objects.property(Boolean::class.java)
    val prerelease: Property<Boolean> = project.objects.property(Boolean::class.java)
    val releaseAssets: ConfigurableFileCollection = project.files()
    val overwrite: Property<Boolean> = project.objects.property(Boolean::class.java)
    val allowUploadToExisting: Property<Boolean> = project.objects.property(Boolean::class.java)

    fun changelog(@DelegatesTo(ChangeLogSupplier::class) closure: Closure<ChangeLogSupplier>) =
            ChangeLogSupplier(this, project).apply { closure.call() }

    fun changelog() = ChangeLogSupplier(this, project)

    fun getOwnerProvider() = owner

    fun getRepoProvider() = repo

    fun getAuthorizationProvider() = authorization

    fun getTagNameProvider() = tagName

    fun getTargetCommitishProvider() = targetCommitish

    fun getReleaseNameProvider() = releaseName

    fun getBodyProvider() = body

    fun getDraftProvider() = draft

    fun getPrereleaseProvider() = prerelease

    fun getOverwriteProvider() = overwrite

    fun getAllowUploadToExistingProvider() = allowUploadToExisting

    fun setOwner(owner: CharSequence) = this.owner.set(owner)

    fun owner(owner: CharSequence) = this.owner.set(owner)

    fun setOwner(owner: Provider<CharSequence>) = this.owner.set(owner)

    fun owner(owner: Provider<CharSequence>) = this.owner.set(owner)

    fun setOwner(owner: Callable<CharSequence>) = this.owner.set(project.provider(owner))

    fun owner(owner: Callable<CharSequence>) = this.owner.set(project.provider(owner))

    fun setRepo(repo: CharSequence) = this.repo.set(repo)

    fun repo(repo: CharSequence) = this.repo.set(repo)

    fun setRepo(repo: Provider<CharSequence>) = this.repo.set(repo)

    fun repo(repo: Provider<CharSequence>) = this.repo.set(repo)

    fun setRepo(repo: Callable<CharSequence>) = this.repo.set(project.provider(repo))

    fun repo(repo: Callable<CharSequence>) = this.repo.set(project.provider(repo))

    fun setToken(token: CharSequence) = this.authorization.set("Token $token")

    fun token(token: CharSequence) = this.authorization.set("Token $token")

    fun setToken(token: Provider<CharSequence>) = this.authorization.set(token.map { "Token $it" })

    fun token(token: Provider<CharSequence>) = this.authorization.set(token.map { "Token $it" })

    fun setToken(token: Callable<CharSequence>) = this.authorization.set(project.provider(token).map { "Token $it" })

    fun token(token: Callable<CharSequence>) = this.authorization.set(project.provider(token).map { "Token $it" })

    fun setAuthorization(authorization: CharSequence) = this.authorization.set(authorization)

    fun authorization(authorization: CharSequence) = this.authorization.set(authorization)

    fun setAuthorization(authorization: Provider<CharSequence>) = this.authorization.set(authorization)

    fun authorization(authorization: Provider<CharSequence>) = this.authorization.set(authorization)

    fun setAuthorization(authorization: Callable<CharSequence>) =
            this.authorization.set(project.provider(authorization))

    fun authorization(authorization: Callable<CharSequence>) = this.authorization.set(project.provider(authorization))

    fun setTagName(tagName: CharSequence) = this.tagName.set(tagName)

    fun tagName(tagName: CharSequence) = this.tagName.set(tagName)

    fun setTagName(tagName: Provider<CharSequence>) = this.tagName.set(tagName)

    fun tagName(tagName: Provider<CharSequence>) = this.tagName.set(tagName)

    fun setTagName(tagName: Callable<CharSequence>) = this.tagName.set(project.provider(tagName))

    fun tagName(tagName: Callable<CharSequence>) = this.tagName.set(project.provider(tagName))

    fun setTargetCommitish(targetCommitish: CharSequence) = this.targetCommitish.set(targetCommitish)

    fun targetCommitish(targetCommitish: CharSequence) = this.targetCommitish.set(targetCommitish)

    fun setTargetCommitish(targetCommitish: Provider<CharSequence>) = this.targetCommitish.set(targetCommitish)

    fun targetCommitish(targetCommitish: Provider<CharSequence>) = this.targetCommitish.set(targetCommitish)

    fun setTargetCommitish(targetCommitish: Callable<CharSequence>) =
            this.targetCommitish.set(project.provider(targetCommitish))

    fun targetCommitish(targetCommitish: Callable<CharSequence>) =
            this.targetCommitish.set(project.provider(targetCommitish))

    fun setReleaseName(releaseName: CharSequence) = this.releaseName.set(releaseName)

    fun releaseName(releaseName: CharSequence) = this.releaseName.set(releaseName)

    fun setReleaseName(releaseName: Provider<CharSequence>) = this.releaseName.set(releaseName)

    fun releaseName(releaseName: Provider<CharSequence>) = this.releaseName.set(releaseName)

    fun setReleaseName(releaseName: Callable<CharSequence>) = this.releaseName.set(project.provider(releaseName))

    fun releaseName(releaseName: Callable<CharSequence>) = this.releaseName.set(project.provider(releaseName))

    fun setBody(body: CharSequence) = this.body.set(body)

    fun body(body: CharSequence) = this.body.set(body)

    fun setBody(body: Provider<CharSequence>) = this.body.set(body)

    fun body(body: Provider<CharSequence>) = this.body.set(body)

    fun setBody(body: Callable<CharSequence>) = this.body.set(project.provider(body))

    fun body(body: Callable<CharSequence>) = this.body.set(project.provider(body))

    fun setDraft(draft: Boolean) = this.draft.set(draft)

    fun draft(draft: Boolean) = this.draft.set(draft)

    fun setDraft(draft: Provider<Boolean>) = this.draft.set(draft)

    fun draft(draft: Provider<Boolean>) = this.draft.set(draft)

    fun setDraft(draft: Callable<Boolean>) = this.draft.set(project.provider(draft))

    fun draft(draft: Callable<Boolean>) = this.draft.set(project.provider(draft))

    fun setPrerelease(prerelease: Boolean) = this.prerelease.set(prerelease)

    fun prerelease(prerelease: Boolean) = this.prerelease.set(prerelease)

    fun setPrerelease(prerelease: Provider<Boolean>) = this.prerelease.set(prerelease)

    fun prerelease(prerelease: Provider<Boolean>) = this.prerelease.set(prerelease)

    fun setPrerelease(prerelease: Callable<Boolean>) = this.prerelease.set(project.provider(prerelease))

    fun prerelease(prerelease: Callable<Boolean>) = this.prerelease.set(project.provider(prerelease))

    fun setReleaseAssets(vararg assets: Any) = this.releaseAssets.setFrom(assets)

    fun releaseAssets(vararg assets: Any) = this.releaseAssets.setFrom(assets)

    fun setOverwrite(replacePrevious: Boolean) = this.overwrite.set(replacePrevious)

    fun overwrite(replacePrevious: Boolean) = this.overwrite.set(replacePrevious)

    fun setOverwrite(replacePrevious: Provider<Boolean>) = this.overwrite.set(replacePrevious)

    fun overwrite(replacePrevious: Provider<Boolean>) = this.overwrite.set(replacePrevious)

    fun setOverwrite(replacePrevious: Callable<Boolean>) = this.overwrite.set(project.provider(replacePrevious))

    fun overwrite(replacePrevious: Callable<Boolean>) = this.overwrite.set(project.provider(replacePrevious))

    fun setAllowUploadToExisting(allowUploadToExisting: Boolean) =
            this.allowUploadToExisting.set(allowUploadToExisting)

    fun allowUploadToExisting(allowUploadToExisting: Boolean) = this.allowUploadToExisting.set(allowUploadToExisting)

    fun setAllowUploadToExisting(allowUploadToExisting: Provider<Boolean>) =
            this.allowUploadToExisting.set(allowUploadToExisting)

    fun allowUploadToExisting(allowUploadToExisting: Provider<Boolean>) =
            this.allowUploadToExisting.set(allowUploadToExisting)

    fun setAllowUploadToExisting(allowUploadToExisting: Callable<Boolean>) =
            this.allowUploadToExisting.set(project.provider(allowUploadToExisting))

    fun allowUploadToExisting(allowUploadToExisting: Callable<Boolean>) =
            this.allowUploadToExisting.set(project.provider(allowUploadToExisting))

}
