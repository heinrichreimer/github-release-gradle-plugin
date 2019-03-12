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

import com.github.breadmoirai.util.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

@Suppress("UnstableApiUsage")
class GithubReleaseTask : DefaultTask() {

    @get:Input
    internal val ownerProperty: Property<CharSequence> = project.objects.property()
    @get:Internal
    var ownerProvider by ownerProperty.providerDelegate
    @get:Internal
    var owner by ownerProperty.valueDelegate

    @get:Input
    internal val repoProperty: Property<CharSequence> = project.objects.property()
    @get:Internal
    var repoProvider by repoProperty.providerDelegate
    @get:Internal
    var repo by repoProperty.valueDelegate

    @get:Input
    internal val authorizationProperty: Property<CharSequence> = project.objects.property()
    @get:Internal
    var authorizationProvider by authorizationProperty.providerDelegate
    @get:Internal
    var authorization by authorizationProperty.valueDelegate
    @get:Internal
    var tokenProvider by authorizationProperty.providerDelegate
    @get:Internal
    var token by authorizationProperty.valueDelegate

    @get:Input
    internal val tagNameProperty: Property<CharSequence> = project.objects.property()
    @get:Internal
    var tagNameProvider by tagNameProperty.providerDelegate
    @get:Internal
    var tagName by tagNameProperty.valueDelegate

    @get:Input
    internal val targetCommitishProperty: Property<CharSequence> = project.objects.property()
    @get:Internal
    var targetCommitishProvider by targetCommitishProperty.providerDelegate
    @get:Internal
    var targetCommitish by targetCommitishProperty.valueDelegate

    @get:Input
    internal val releaseNameProperty: Property<CharSequence> = project.objects.property()
    @get:Internal
    var releaseNameProvider by releaseNameProperty.providerDelegate
    @get:Internal
    var releaseName by releaseNameProperty.valueDelegate

    @get:Input
    internal val bodyProperty: Property<CharSequence> = project.objects.property()
    @get:Internal
    var bodyProvider by bodyProperty.providerDelegate
    @get:Internal
    var body by bodyProperty.valueDelegate

    @get:Input
    internal val draftProperty: Property<Boolean> = project.objects.property()
    @get:Internal
    var draftProvider by draftProperty.providerDelegate
    @get:Internal
    var draft by draftProperty.valueDelegate

    @get:Input
    internal val prereleaseProperty: Property<Boolean> = project.objects.property()
    @get:Internal
    var prereleaseProvider by prereleaseProperty.providerDelegate
    @get:Internal
    var prerelease by prereleaseProperty.valueDelegate

    @get:InputFiles
    internal val releaseAssetsFileCollection: ConfigurableFileCollection = project.files()
    @get:Internal
    var releaseAssetsCollection by releaseAssetsFileCollection.collectionDelegate
    @get:Internal
    var releaseAssets by releaseAssetsFileCollection.filesDelegate
    @get:Internal
    var releaseAsset by releaseAssetsFileCollection.fileDelegate

    fun releaseAssets(vararg assets: Any) = releaseAssetsFileCollection.setFrom(assets)

    @get:Input
    internal val overwriteProperty: Property<Boolean> = project.objects.property()
    @get:Internal
    var overwriteProvider by overwriteProperty.providerDelegate
    @get:Internal
    var overwrite by overwriteProperty.valueDelegate

    @get:Input
    internal val allowUploadToExistingProperty: Property<Boolean> = project.objects.property()
    @get:Internal
    var allowUploadToExistingProvider by allowUploadToExistingProperty.providerDelegate
    @get:Internal
    var allowUploadToExisting by allowUploadToExistingProperty.valueDelegate

    init {
        group = "publishing"
    }

    @TaskAction
    fun publishRelease() {
        GithubRelease(
                owner = ownerProvider,
                repo = repoProperty,
                authorization = authorizationProperty.map { "Token $it" },
                tagName = tagNameProperty,
                targetCommitish = targetCommitishProperty,
                releaseName = releaseNameProperty,
                body = bodyProperty,
                draft = draftProperty,
                prerelease = prereleaseProperty,
                releaseAssets = releaseAssetsFileCollection,
                overwrite = overwriteProperty,
                allowUploadToExisting = allowUploadToExistingProperty
        ).run()
    }
}
