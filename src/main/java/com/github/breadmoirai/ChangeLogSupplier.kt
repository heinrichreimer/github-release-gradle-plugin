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

import groovy.json.JsonSlurper
import okhttp3.OkHttpClient
import okhttp3.Response
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.zeroturnaround.exec.ProcessExecutor
import java.io.IOException
import java.util.concurrent.Callable

@Suppress("UnstableApiUsage")
class ChangeLogSupplier(extension: GithubReleaseExtension, private val project: Project) : Callable<String> {

    companion object {
        private val log : Logger= LoggerFactory.getLogger(ChangeLogSupplier::class.java)
    }

    private val  owner: Provider<CharSequence> = extension.getOwnerProvider()
    private val repo: Provider<CharSequence> = extension.getRepoProvider()
    private val authorization: Provider<CharSequence> = extension.getAuthorizationProvider()
    private val tag: Provider<CharSequence> = extension.getTagNameProvider()

    private val executable: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    private val currentCommit: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    private val lastCommit: Property<CharSequence> = project.objects.property(CharSequence::class.java)
    private val options: Property<List<*>> = project.objects.property(List::class.java)

    init {
        setExecutable("git")
        setCurrentCommit("HEAD")
        setLastCommit(Callable { this.getLastReleaseCommit() })
        setOptions(listOf("--format=oneline", "--abbrev-commit", "--max-count=50"))
    }

    /**
     * Looks for the previous release's targetCommitish
     * @return
     */
    private fun getLastReleaseCommit(): CharSequence {
        val owner = this.owner.orNull
                ?: throw  PropertyNotSetException("owner")
        val repo = this.repo.orNull
                ?: throw  PropertyNotSetException("repo")
        val auth = authorization.orNull
                ?: throw  PropertyNotSetException("auth")
        val tag = this.tag.orNull
                ?: throw  PropertyNotSetException("tag")

        // query the github api for releases
        val releaseUrl = "https://api.github.com/repos/$owner/$repo/releases"
        val response:Response =  OkHttpClient().newCall(GithubRelease.createRequestWithHeaders(auth)
                .url(releaseUrl)
                .get()
                .build()
        ).execute()
        if (response.code() != 200) {
            return ""
        }
        val releases:List<Any> =  JsonSlurper().parse(response.body()?.bytes()) as List<Any>
        // find current release if exists
        val index = releases.indexOfFirst { release -> (release as Map<String, Any>)["tag_name"] == tag }
        if (releases.isEmpty()) {
            val exe = this.executable.orNull
                    ?: throw  PropertyNotSetException("exe")
            val cmd = listOf(exe.toString(), "rev-list", "--max-parents=0", "--max-count=1", "HEAD")
            return ProcessExecutor()
                    .command(cmd)
                    .readOutput(true)
                    .exitValueNormal()
                    .execute()
                    .outputUTF8()
                    .trim()
        } else {
            // get the next release before the current release
            // if current release does not ezist, then gets the most recent release
            val lastRelease = releases[index + 1] as Map<String, Any>
            val lastTag = lastRelease["tag_name"]
            val tagUrl = "https://api.github.com/repos/$owner/$repo/git/refs/tags/$lastTag"
            val tagResponse: Response =  OkHttpClient()
                    .newCall(GithubRelease.createRequestWithHeaders(auth)
                    .url(tagUrl)
                    .get()
                    .build()
            ).execute()

            // retrieves the sha1 commit from the response
            val bodyS = tagResponse.body()?.bytes()
            tagResponse.body()?.close()
            return ((JsonSlurper().parse(bodyS) as Map<String, Any>)["object"] as Map<String, Any>)["sha"].toString()
        }

    }

    override fun     call(): String {
        log.info(":githubRelease Generating Release Body with Commit History")
        val current = currentCommit.get()
        val last = lastCommit.get()
        val opts = options.get().map { it.toString() }.toTypedArray()
        val get = executable.orNull?.toString()
                ?: throw  PropertyNotSetException("get")
        val cmds = listOf(get, "rev-list", *opts, "$last..$current", "--")
        try {
            return  ProcessExecutor()
                    .command(cmds)
                    .readOutput(true)
                    .exitValueNormal()
                    .execute()
                    .outputUTF8()
        } catch (e:IOException) {
            if (e.cause != null && e.cause?.message?.contains("CreateProcess error=2") == true) {
                throw  Error("Failed to run git executable to find commit history. " +
                        "Please specify the path to the git executable.\n")
            }
            throw e
        }
    }

    fun setCurrentCommit( currentCommit:Provider<CharSequence>) {
        this.currentCommit.set(currentCommit)
    }

    fun currentCommit(currentCommit : Provider<CharSequence>) {
        this.currentCommit.set(currentCommit)
    }

    fun setLastCommit(lastCommit : Provider<CharSequence>) {
        this.lastCommit.set(lastCommit)
    }

    fun lastCommit(lastCommit : Provider<CharSequence>) {
        this.lastCommit.set(lastCommit)
    }

    fun setOptions(options : Provider<List<Any>>) {
        this.options.set(options)
    }

    fun options(options : Provider<List<Any>>) {
        this.options.set(options)
    }

    fun setExecutable(gitExecutable : Provider<CharSequence>) {
        this.executable.set(gitExecutable)
    }

    fun executable(gitExecutable : Provider<CharSequence>) {
        this.executable.set(gitExecutable)
    }

    fun setCurrentCommit(currentCommit : Callable<CharSequence>) {
        setCurrentCommit(project.provider(currentCommit))
    }

    fun currentCommit(currentCommit : Callable<CharSequence>) {
        setCurrentCommit(project.provider(currentCommit))
    }

    fun setLastCommit(lastCommit : Callable<CharSequence>) {
        setLastCommit(project.provider(lastCommit))
    }

    fun lastCommit(lastCommit : Callable<CharSequence>) {
        setLastCommit(project.provider(lastCommit))
    }

    fun setOptions(options : Callable<List<Any>>) {
        setOptions(project.provider(options))
    }

    fun options(options : Callable<List<Any>>) {
        setOptions(project.provider(options))
    }

    fun setExecutable(gitExecutable : Callable<CharSequence>) {
        setExecutable(project.provider(gitExecutable))
    }

    fun executable(gitExecutable : Callable<CharSequence>) {
        setExecutable(project.provider(gitExecutable))
    }

    fun setCurrentCommit(currentCommit : CharSequence) {
        setCurrentCommit(Callable { currentCommit })
    }

    fun currentCommit(currentCommit : CharSequence) {
        setCurrentCommit(Callable { currentCommit })
    }

    fun setLastCommit(lastCommit : CharSequence) {
        setLastCommit(Callable { lastCommit })
    }

    fun lastCommit(lastCommit : CharSequence) {
        setLastCommit(Callable { lastCommit })
    }

    fun setOptions(options : List<Any>) {
        setOptions(Callable { options })
    }

    fun options( options: List<Any>) {
        setOptions(Callable { options })
    }

    fun setExecutable( gitExecutable:CharSequence) {
        setExecutable(Callable { gitExecutable })
    }

    fun executable( gitExecutable:CharSequence) {
        setExecutable(Callable { gitExecutable })
    }
}