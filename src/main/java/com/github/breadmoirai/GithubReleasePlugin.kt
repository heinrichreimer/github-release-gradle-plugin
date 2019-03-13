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

@Suppress("UnstableApiUsage")
class GithubReleasePlugin : Plugin<Project> {

    companion object {
        private const val EXTENSION_NAME = "githubRelease"
        private const val TASK_NAME = "githubRelease"
        private val log: Logger = Logging.getLogger(GithubReleasePlugin::class.java)
    }

    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project

        val githubReleaseExtension = createExtension()

        githubReleaseExtension.setDefaults(project)

        log.debug("Registering $TASK_NAME task for ${GithubReleasePlugin::class.java.simpleName}.")
        project.tasks
                .register(
                        TASK_NAME,
                        GithubReleaseTask::class.java,
                        project.objects,
                        project.layout,
                        project.providers
                )
                .configure {
                    it.copyFrom(githubReleaseExtension)
                }
    }

    private fun createExtension(): GithubReleaseExtension {
        log.debug("Creating $EXTENSION_NAME extension for ${GithubReleasePlugin::class.java.simpleName}.")
        return project.extensions
                .create(
                        EXTENSION_NAME,
                        GithubReleaseExtension::class.java,
                        project.objects,
                        project.layout,
                        project.providers
                )
    }

    private fun GithubReleaseExtension.setDefaults(project: Project) {
        log.debug("Assigning default values for ${GithubReleasePlugin::class.java.simpleName}.")
        owner {
            project.group
                    .toString()
                    .substringAfterLast('.')
        }
        repo {
            project.name
        }
        tagName {
            "v${project.version}"
        }
        targetCommitish = "master"
        releaseNameProvider = tagNameProvider
        draft = false
        prerelease = false
        authorization {
            throw IllegalArgumentException("Must specify GitHub authorization token.")
        }
        bodyProvider = changelog
        overwrite = false
        allowUploadToExisting = false
    }
}
