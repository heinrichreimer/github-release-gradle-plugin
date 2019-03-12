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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property

import java.util.concurrent.Callable

class GithubReleasePlugin : Plugin<Project> {

    companion object {
        private val log: Logger = Logging.getLogger(GithubReleasePlugin::class.java)
        var infoEnabled = false
    }

    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project
        infoEnabled = project.logger.isInfoEnabled

        log.debug("Creating Extension githubRelease")
        val ext = project.extensions.create("githubRelease", GithubReleaseExtension::class.java, project)

        project.tasks.create("githubRelease", GithubReleaseTask::class.java) {
            log.debug("Creating Task githubRelease")
            it.apply {
                setAuthorization(ext.getAuthorizationProvider())
                setOwner(ext.getOwnerProvider())
                setRepo(ext.getRepoProvider())
                setTagName(ext.getTagNameProvider())
                setTargetCommitish(ext.getTargetCommitishProvider())
                setReleaseName(ext.getReleaseNameProvider())
                setBody(ext.getBodyProvider())
                setDraft(ext.getDraftProvider())
                setPrerelease(ext.getPrereleaseProvider())
                setReleaseAssets(ext.releaseAssets)
                setOverwrite(ext.getOverwriteProvider())
                setAllowUploadToExisting(ext.getAllowUploadToExistingProvider())
            }
        }

        project.afterEvaluate {
            val self = project.plugins.findPlugin(GithubReleasePlugin::class.java)

            if (self != null) {
                log.debug("Assigning default values for GithubReleasePlugin")
                val e: GithubReleaseExtension = project.extensions.getByType(GithubReleaseExtension::class.java)
                setOrElse("owner", e.owner, CharSequence::class.java, Callable<CharSequence> {
                    val group = project.group.toString()
                    group.substring(group.lastIndexOf('.') + 1)
                })
                setOrElse("repo", e.repo, CharSequence::class.java, Callable<CharSequence> {
                    project.name ?: project.rootProject.name ?: project.rootProject.rootProject.name
                })
                setOrElse("tagName", e.tagName, CharSequence::class.java, Callable<CharSequence> {
                    "v${project.version}"
                })
                setOrElse("targetCommitish", e.targetCommitish, CharSequence::class.java, Callable<CharSequence> {
                    "master"
                })
                setOrElse("releaseName", e.releaseName, CharSequence::class.java, Callable {
                    e.tagName.get()
                })
                setOrElse("draft", e.draft, Boolean::class.java, Callable { false })
                setOrElse("prerelease", e.prerelease, Boolean::class.java, Callable { false })
                setOrElse("authorization", e.authorization, CharSequence::class.java, Callable<CharSequence> {
                    //new GithubLoginApp().awaitResult().map{result -> "Basic $result"}.get()
                    null
                })
                setOrElse("body", e.body, CharSequence::class.java, Callable<CharSequence> {
                    ChangeLogSupplier(e, project).call()
                })
                setOrElse("overwrite", e.overwrite, Boolean::class.java, Callable { false })
                setOrElse("allowUploadToExisting", e.allowUploadToExisting, Boolean::class.java, Callable { false })
            }

        }
    }

    private fun <T> setOrElse(name: String, prop: Property<T>, type: Class<T>, value: Callable<T>) {
        if (!prop.isPresent) {
            prop.set(project.provider(value))
        }
    }

}
