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
