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

import com.github.breadmoirai.configuration.copyFrom
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class GithubReleasePlugin : Plugin<Project> {

    companion object {
        private const val EXTENSION_NAME = "githubRelease"
        private const val TASK_NAME = "githubRelease"
        private val log: Logger = Logging.getLogger(GithubReleasePlugin::class.java)
    }

    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project

        val githubReleaseExtension = project
                .extensions
                .create(EXTENSION_NAME, GithubReleaseExtension::class.java, project)

        project.tasks.create(TASK_NAME, GithubReleaseTask::class.java) {
            it.copyFrom(githubReleaseExtension)
        }

        project.afterEvaluate {
            val self = project.plugins.findPlugin(GithubReleasePlugin::class.java)

            if (self != null) {
                log.debug("Assigning default values for ${GithubReleasePlugin::class.java.simpleName}")
                val extension: GithubReleaseExtension = project
                        .extensions
                        .getByType(GithubReleaseExtension::class.java)
//                extension.owner.setOrElse(Callable<CharSequence> {
//                    val group = project.group.toString()
//                    group.substring(group.lastIndexOf('.') + 1)
//                })
//                extension.repo.setOrElse(Callable<CharSequence> {
//                    project.name ?: project.rootProject.name ?: project.rootProject.rootProject.name
//                })
//                extension.tagName.setOrElse(Callable<CharSequence> {
//                    "v${project.version}"
//                })
//                extension.targetCommitish.setOrElse(Callable<CharSequence> {
//                    "master"
//                })
//                extension.releaseName.setOrElse(Callable {
//                    extension.tagName.get()
//                })
//                extension.draft.setOrElse(Callable { false })
//                extension.prerelease.setOrElse(Callable { false })
//                extension.authorization.setOrElse(Callable<CharSequence> {
//                    //new GithubLoginApp().awaitResult().map{result -> "Basic $result"}.get()
//                    null
//                })
//                extension.body.setOrElse(Callable<CharSequence> {
//                    ChangeLogSupplier(extension, project).call()
//                })
//                extension.overwrite.setOrElse(Callable { false })
//                extension.allowUploadToExisting.setOrElse(Callable { false })
            }

        }
    }

//    private fun <T> Property<T>.setOrElse(value: Callable<T>) {
//        if (!isPresent) {
//            set(project.provider(value))
//        }
//    }

}
