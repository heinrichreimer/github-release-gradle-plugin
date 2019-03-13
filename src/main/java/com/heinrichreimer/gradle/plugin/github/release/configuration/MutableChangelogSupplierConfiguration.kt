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

package com.heinrichreimer.gradle.plugin.github.release.configuration

import org.gradle.api.provider.Provider

interface MutableChangelogSupplierConfiguration : ChangelogSupplierConfiguration {

    override var gitExecutableProvider: Provider<String>
    override var gitExecutable: String

    fun gitExecutable(gitExecutable: String) {
        this.gitExecutable = gitExecutable
    }

    fun gitExecutable(gitExecutable: Provider<String>) {
        gitExecutableProvider = gitExecutable
    }

    fun gitExecutable(gitExecutable: () -> String)

    override var currentCommitProvider: Provider<String>
    override var currentCommit: String

    fun currentCommit(currentCommit: String) {
        this.currentCommit = currentCommit
    }

    fun currentCommit(currentCommit: Provider<String>) {
        currentCommitProvider = currentCommit
    }

    fun currentCommit(currentCommit: () -> String)

    override var lastCommitProvider: Provider<String>
    override var lastCommit: String

    fun lastCommit(lastCommit: String) {
        this.lastCommit = lastCommit
    }

    fun lastCommit(lastCommit: Provider<String>) {
        lastCommitProvider = lastCommit
    }

    fun lastCommit(lastCommit: () -> String)

    override var gitOptionsProvider: Provider<Iterable<Any>>
    override var gitOptions: Iterable<Any>

    fun gitOptions(gitOptions: Iterable<Any>) {
        this.gitOptions = gitOptions
    }

    fun gitOptions(gitOptions: Provider<Iterable<Any>>) {
        gitOptionsProvider = gitOptions
    }

    fun gitOptions(gitOptions: () -> Iterable<Any>)
}
