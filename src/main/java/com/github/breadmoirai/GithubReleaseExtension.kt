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

import com.github.breadmoirai.configuration.MutableGithubReleaseConfiguration
import com.github.breadmoirai.util.collectionDelegate
import com.github.breadmoirai.util.property
import com.github.breadmoirai.util.providerDelegate
import com.github.breadmoirai.util.valueDelegate
import groovy.lang.Closure
import groovy.lang.DelegatesTo
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Internal

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
class GithubReleaseExtension(
        private val objects: ObjectFactory,
        private val layout: ProjectLayout,
        private val providers: ProviderFactory
) : MutableGithubReleaseConfiguration {

    constructor(project: Project) : this(
            project.objects,
            project.layout,
            project.providers
    )

    @get:Internal
    internal val ownerProperty: Property<CharSequence> = objects.property()
    override var ownerProvider by ownerProperty.providerDelegate
    override var owner by ownerProperty.valueDelegate
    override fun owner(owner: () -> CharSequence) {
        ownerProvider = providers.provider { owner() }
    }

    @get:Internal
    internal val repoProperty: Property<CharSequence> = objects.property()
    override var repoProvider by repoProperty.providerDelegate
    override var repo by repoProperty.valueDelegate
    override fun repo(repo: () -> CharSequence) {
        repoProvider = providers.provider { repo() }
    }

    @get:Internal
    internal val authorizationProperty: Property<CharSequence> = objects.property()
    override var authorizationProvider by authorizationProperty.providerDelegate
    override var authorization by authorizationProperty.valueDelegate
    override fun authorization(authorization: () -> CharSequence) {
        authorizationProvider = providers.provider { authorization() }
    }

    @get:Internal
    internal val tagNameProperty: Property<CharSequence> = objects.property()
    override var tagNameProvider by tagNameProperty.providerDelegate
    override var tagName by tagNameProperty.valueDelegate
    override fun tagName(tagName: () -> CharSequence) {
        tagNameProvider = providers.provider { tagName() }
    }

    @get:Internal
    internal val targetCommitishProperty: Property<CharSequence> = objects.property()
    override var targetCommitishProvider by targetCommitishProperty.providerDelegate
    override var targetCommitish by targetCommitishProperty.valueDelegate
    override fun targetCommitish(targetCommitish: () -> CharSequence) {
        targetCommitishProvider = providers.provider { targetCommitish() }
    }

    @get:Internal
    internal val releaseNameProperty: Property<CharSequence> = objects.property()
    override var releaseNameProvider by releaseNameProperty.providerDelegate
    override var releaseName by releaseNameProperty.valueDelegate
    override fun releaseName(releaseName: () -> CharSequence) {
        releaseNameProvider = providers.provider { releaseName() }
    }

    @get:Internal
    internal val bodyProperty: Property<CharSequence> = objects.property()
    override var bodyProvider by bodyProperty.providerDelegate
    override var body by bodyProperty.valueDelegate
    override fun body(body: () -> CharSequence) {
        bodyProvider = providers.provider { body() }
    }

    @get:Internal
    internal val draftProperty: Property<Boolean> = objects.property()
    override var draftProvider by draftProperty.providerDelegate
    override var draft by draftProperty.valueDelegate
    override fun draft(draft: () -> Boolean) {
        draftProvider = providers.provider { draft() }
    }

    @get:Internal
    internal val prereleaseProperty: Property<Boolean> = objects.property()
    override var prereleaseProvider by prereleaseProperty.providerDelegate
    override var prerelease by prereleaseProperty.valueDelegate
    override fun prerelease(prerelease: () -> Boolean) {
        prereleaseProvider = providers.provider { prerelease() }
    }

    @get:Internal
    internal val releaseAssetsFileCollection: ConfigurableFileCollection = layout.configurableFiles()
    override var releaseAssets by releaseAssetsFileCollection.collectionDelegate

    @get:Internal
    internal val overwriteProperty: Property<Boolean> = objects.property()
    override var overwriteProvider by overwriteProperty.providerDelegate
    override var overwrite by overwriteProperty.valueDelegate
    override fun overwrite(overwrite: () -> Boolean) {
        overwriteProvider = providers.provider { overwrite() }
    }

    @get:Internal
    internal val allowUploadToExistingProperty: Property<Boolean> = objects.property()
    override var allowUploadToExistingProvider by allowUploadToExistingProperty.providerDelegate
    override var allowUploadToExisting by allowUploadToExistingProperty.valueDelegate
    override fun allowUploadToExisting(allowUploadToExisting: () -> Boolean) {
        allowUploadToExistingProvider = providers.provider { allowUploadToExisting() }
    }

    fun changelog(@DelegatesTo(ChangeLogSupplier::class) closure: Closure<ChangeLogSupplier>) =
            changelog().apply { closure.call() }

    fun changelog() = ChangeLogSupplier(this, objects, layout, providers)

}
