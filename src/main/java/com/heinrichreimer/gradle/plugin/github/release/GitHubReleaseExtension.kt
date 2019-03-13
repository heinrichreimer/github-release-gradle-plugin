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

package com.heinrichreimer.gradle.plugin.github.release

import com.heinrichreimer.gradle.plugin.github.release.configuration.MutableChangelogSupplierConfiguration
import com.heinrichreimer.gradle.plugin.github.release.configuration.MutableGitHubReleaseConfiguration
import com.heinrichreimer.gradle.plugin.github.release.configuration.UpdateMode
import com.heinrichreimer.gradle.plugin.github.release.util.collectionDelegate
import com.heinrichreimer.gradle.plugin.github.release.util.property
import com.heinrichreimer.gradle.plugin.github.release.util.providerDelegate
import com.heinrichreimer.gradle.plugin.github.release.util.valueDelegate
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
 * An extension for the [GitHubReleasePlugin]
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
 *             <td>repository</td>
 *             <td>project.name ||<br />
 *                 rootProject.name</td>
 *         </tr>
 *         <tr>
 *             <td>tag</td>
 *             <td>'v' + project.version</td>
 *         </tr>
 *         <tr>
 *             <td>target</td>
 *             <td>'master'</td>
 *         </tr>
 *         <tr>
 *             <td>title</td>
 *             <td>'v' + project.version</td>
 *         </tr>
 *         <tr>
 *             <td>body</td>
 *             <td>list of commits since last release</td>
 *         </tr>
 *         <tr>
 *             <td>isDraft</td>
 *             <td>false</td>
 *         </tr>
 *         <tr>
 *             <td>isPreRelease</td>
 *             <td>false</td>
 *         </tr>
 *         <tr>
 *             <td>authorization</td>
 *             <td>N/A</td>
 *         </tr>
 *         <tr>
 *             <td>updateMode</td>
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
open class GitHubReleaseExtension(
        objects: ObjectFactory,
        layout: ProjectLayout,
        private val providers: ProviderFactory
) : MutableGitHubReleaseConfiguration {

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
    override var repositoryProvider by repoProperty.providerDelegate
    override var repository by repoProperty.valueDelegate
    override fun repository(repository: () -> String) {
        repositoryProvider = providers.provider { repository() }
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
    override var tagProvider by tagNameProperty.providerDelegate
    override var tag by tagNameProperty.valueDelegate
    override fun tag(tag: () -> String) {
        tagProvider = providers.provider { tag() }
    }

    @get:Internal
    internal val targetCommitishProperty: Property<String> = objects.property()
    override var targetProvider by targetCommitishProperty.providerDelegate
    override var target by targetCommitishProperty.valueDelegate
    override fun target(target: () -> String) {
        targetProvider = providers.provider { target() }
    }

    @get:Internal
    internal val releaseNameProperty: Property<String> = objects.property()
    override var titleProvider by releaseNameProperty.providerDelegate
    override var title by releaseNameProperty.valueDelegate
    override fun title(title: () -> String) {
        titleProvider = providers.provider { title() }
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
    override var isDraftProvider by draftProperty.providerDelegate
    override var isDraft by draftProperty.valueDelegate
    override fun isDraft(isDraft: () -> Boolean) {
        isDraftProvider = providers.provider { isDraft() }
    }

    @get:Internal
    internal val prereleaseProperty: Property<Boolean> = objects.property()
    override var isPreReleaseProvider by prereleaseProperty.providerDelegate
    override var isPreRelease by prereleaseProperty.valueDelegate
    override fun isPreRelease(isPreRelease: () -> Boolean) {
        isPreReleaseProvider = providers.provider { isPreRelease() }
    }

    @get:Internal
    internal val releaseAssetsFileCollection: ConfigurableFileCollection = layout.configurableFiles()
    override var releaseAssets by releaseAssetsFileCollection.collectionDelegate

    override fun releaseAssets(releaseAssetPaths: Iterable<*>) =
            releaseAssetsFileCollection.setFrom(releaseAssetPaths)

    override fun releaseAssets(vararg releaseAssetPaths: Any) =
            releaseAssetsFileCollection.setFrom(releaseAssetPaths)

    @get:Internal
    internal val overwriteProperty: Property<UpdateMode> = objects.property()
    override var updateModeProvider by overwriteProperty.providerDelegate
    override var updateMode by overwriteProperty.valueDelegate
    override fun updateMode(updateMode: () -> UpdateMode) {
        updateModeProvider = providers.provider { updateMode() }
    }

    internal val changeLogSupplier = ChangelogSupplier(this, objects, layout, providers)

    val changelog: Provider<String>
        get() = providers.provider {
            changeLogSupplier.call()
        }

    fun changelog(
            @DelegatesTo(MutableChangelogSupplierConfiguration::class)
            closure: Closure<MutableChangelogSupplierConfiguration>
    ): Provider<String> {
        return providers.provider {
            changeLogSupplier
                    .apply { closure.call() }
                    .call()
        }
    }

    fun changelog(action: Action<MutableChangelogSupplierConfiguration>): Provider<String> {
        return providers.provider {
            changeLogSupplier
                    .also(action::execute)
                    .call()
        }
    }

    fun changelog(block: (MutableChangelogSupplierConfiguration) -> Unit): Provider<String> {
        return providers.provider {
            changeLogSupplier
                    .also(block)
                    .call()
        }
    }

}
