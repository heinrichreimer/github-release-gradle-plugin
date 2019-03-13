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
/*
 * Based on github-release-gradle-plugin by Ton Ly (@BreadMoirai)
 * Licensed under the Apache License v2.0:
 * https://github.com/BreadMoirai/github-release-gradle-plugin
 */

package com.github.breadmoirai

import com.github.breadmoirai.configuration.MutableChangeLogSupplierConfiguration
import com.github.breadmoirai.configuration.MutableGithubReleaseConfiguration
import com.github.breadmoirai.util.collectionDelegate
import com.github.breadmoirai.util.property
import com.github.breadmoirai.util.providerDelegate
import com.github.breadmoirai.util.valueDelegate
import groovy.lang.Closure
import groovy.lang.DelegatesTo
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
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
@Suppress("UnstableApiUsage")
class GithubReleaseExtension(
        objects: ObjectFactory,
        layout: ProjectLayout,
        private val providers: ProviderFactory
) : MutableGithubReleaseConfiguration {

    constructor(project: Project) : this(
            project.objects,
            project.layout,
            project.providers
    )

    @get:Internal
    internal val ownerProperty: Property<String> = objects.property()
    override var ownerProvider by ownerProperty.providerDelegate
    override var owner by ownerProperty.valueDelegate
    override fun owner(owner: () -> String) {
        ownerProvider = providers.provider { owner() }
    }

    @get:Internal
    internal val repoProperty: Property<String> = objects.property()
    override var repoProvider by repoProperty.providerDelegate
    override var repo by repoProperty.valueDelegate
    override fun repo(repo: () -> String) {
        repoProvider = providers.provider { repo() }
    }

    @get:Internal
    internal val authorizationProperty: Property<String> = objects.property()
    override var authorizationProvider by authorizationProperty.providerDelegate
    override var authorization by authorizationProperty.valueDelegate
    override fun authorization(authorization: () -> String) {
        authorizationProvider = providers.provider { authorization() }
    }

    @get:Internal
    internal val tagNameProperty: Property<String> = objects.property()
    override var tagNameProvider by tagNameProperty.providerDelegate
    override var tagName by tagNameProperty.valueDelegate
    override fun tagName(tagName: () -> String) {
        tagNameProvider = providers.provider { tagName() }
    }

    @get:Internal
    internal val targetCommitishProperty: Property<String> = objects.property()
    override var targetCommitishProvider by targetCommitishProperty.providerDelegate
    override var targetCommitish by targetCommitishProperty.valueDelegate
    override fun targetCommitish(targetCommitish: () -> String) {
        targetCommitishProvider = providers.provider { targetCommitish() }
    }

    @get:Internal
    internal val releaseNameProperty: Property<String> = objects.property()
    override var releaseNameProvider by releaseNameProperty.providerDelegate
    override var releaseName by releaseNameProperty.valueDelegate
    override fun releaseName(releaseName: () -> String) {
        releaseNameProvider = providers.provider { releaseName() }
    }

    @get:Internal
    internal val bodyProperty: Property<String> = objects.property()
    override var bodyProvider by bodyProperty.providerDelegate
    override var body by bodyProperty.valueDelegate
    override fun body(body: () -> String) {
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

    private val changeLogSupplier = ChangeLogSupplier(this, objects, layout, providers)

    val changelog: Provider<String>
        get() = providers.provider {
            changeLogSupplier.call()
        }

    fun changelog(
            @DelegatesTo(MutableChangeLogSupplierConfiguration::class)
            closure: Closure<MutableChangeLogSupplierConfiguration>
    ): Provider<String> {
        return providers.provider {
            changeLogSupplier
                    .apply { closure.call() }
                    .call()
        }
    }

    fun changelog(action: Action<MutableChangeLogSupplierConfiguration>): Provider<String> {
        return providers.provider {
            changeLogSupplier
                    .also(action::execute)
                    .call()
        }
    }

    fun changelog(block: (MutableChangeLogSupplierConfiguration) -> Unit): Provider<String> {
        return providers.provider {
            changeLogSupplier
                    .also(block)
                    .call()
        }
    }

}
