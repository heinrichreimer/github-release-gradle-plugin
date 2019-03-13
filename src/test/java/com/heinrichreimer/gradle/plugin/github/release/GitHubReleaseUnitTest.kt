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

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.zeroturnaround.exec.ProcessExecutor

object GitHubReleaseUnitTest : Spek({

    group("Test 1") {
        val changeLogSupplier: ChangeLogSupplier by memoized {
            (TODO() as ChangeLogSupplier).apply {
                lastCommit {

                    val lastTag = "test"
                    val lastCommit = ProcessExecutor()
                            .command("git", "rev-parse", "--verify", lastTag + "^{commit}")
                            .readOutput(true)
                            .exitValueNormal()
                            .execute()
                            .outputUTF8()
                            .trim()
                    lastCommit
                }
                options {
                    listOf("--format=oneline", "--abbrev-commit", "--max-count=50", "--graph")
                }

            }
        }

        describe("a changelog supplier") {
            it("should return a changlog??") {
                val get = changeLogSupplier.call()
                println("get = $get")
            }
        }
    }

    group("delete release") {
        //        given: "A release with a tag"
//        val app = new GithubRelease("BreadMoirai", "github-release-gradle-plugin")
//
//        when:
//        def result = GradleRunner.create()
//                .withProjectDir(testProjectDir.root)
//                .withArguments('githubRelease')
//                .withPluginClasspath(pluginClasspath)
//                .build()
//
//        then:
//        result.task(":githubRelease").outcome == SUCCESS
//        println "result.output = $result.output"
//
    }
})
