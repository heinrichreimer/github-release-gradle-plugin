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

import com.github.breadmoirai.api.GitHubApiService
import com.github.breadmoirai.configuration.GithubReleaseConfiguration
import com.github.breadmoirai.configuration.MutableChangeLogSupplierConfiguration
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ProviderFactory
import org.zeroturnaround.exec.ProcessExecutor
import java.io.IOException
import java.util.concurrent.Callable

@Suppress("UnstableApiUsage")
class ChangeLogSupplier(
        configuration: GithubReleaseConfiguration,
        private val objects: ObjectFactory,
        private val layout: ProjectLayout,
        private val providers: ProviderFactory
) :
        Callable<String>,
        MutableChangeLogSupplierConfiguration by ChangeLogSupplierExtension(objects, providers),
        GithubReleaseConfiguration by configuration {

    constructor(
            configuration: GithubReleaseConfiguration,
            project: Project
    ) : this(
            configuration = configuration,
            objects = project.objects,
            layout = project.layout,
            providers = project.providers
    )

    companion object {
        private val log: Logger = Logging.getLogger(ChangeLogSupplier::class.java)
    }

    private val service = GitHubApiService(authorizationProvider)

    init {
        executable = "git"
        currentCommit = "HEAD"
        lastCommit { getLastReleaseCommit() }
        options = listOf("--format=oneline", "--abbrev-commit", "--max-count=50")
    }

    /**
     * Looks for the previous release's target commit.
     */
    private fun getLastReleaseCommit(): CharSequence {
        val owner = owner
        val repo = repo


        // query the github api for releases
        val releaseUrl = "https://api.github.com/repos/$owner/$repo/releases"
        val response = service
                .getReleases(owner.toString(), repo.toString())
                .execute()

        if (response.code() != 200) {
            return ""
        }

        val releases = response.body() ?: return ""

        // find current release if exists
        val index = releases.indexOfFirst { release -> release.tag_name == tagName }
        if (releases.isEmpty()) {
            val cmd = listOf(executable.toString(), "rev-list", "--max-parents=0", "--max-count=1", "HEAD")

            return executeAndGetOutput(cmd).trim()
        } else {
            // get the next release before the current release
            // if current release does not ezist, then gets the most recent release
            val lastRelease = releases[index + 1]
            val lastTag = lastRelease.tag_name

            val tagResponse = service.getGitReferenceByTagName(owner.toString(), repo.toString(), lastTag)
                    .execute()

            return tagResponse.body()?.referenced_object?.sha ?: ""
        }
    }

    private fun executeAndGetOutput(commands: Iterable<Any>): String {
        return ProcessExecutor()
                .directory(layout.projectDirectory.asFile)
                .command(commands.map { it.toString() })
                .readOutput(true)
                .exitValueNormal()
                .execute()
                .outputUTF8()
    }

    override fun call(): String {
        log.info("Generating release body using commit history.")
        val opts = options.map { it.toString() }.toTypedArray()
        val cmds = listOf(executable.toString(), "rev-list", *opts, "$lastCommit..$currentCommit", "--")
        try {
            return executeAndGetOutput(cmds)
        } catch (e: IOException) {
            if (e.cause != null && e.cause?.message?.contains("CreateProcess error=2") == true) {
                throw  Error("Failed to run git executable to find commit history. " +
                        "Please specify the path to the git executable.\n")
            }
            throw e
        }
    }
}