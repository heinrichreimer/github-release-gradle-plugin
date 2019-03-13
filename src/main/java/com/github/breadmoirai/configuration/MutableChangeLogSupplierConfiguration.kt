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

package com.github.breadmoirai.configuration

import org.gradle.api.provider.Provider

interface MutableChangeLogSupplierConfiguration : ChangeLogSupplierConfiguration {

    override var executableProvider: Provider<CharSequence>
    override var executable: CharSequence

    fun executable(executable: CharSequence) {
        this.executable = executable
    }

    fun executable(executable: Provider<CharSequence>) {
        executableProvider = executable
    }

    fun executable(executable: () -> CharSequence)

    override var currentCommitProvider: Provider<CharSequence>
    override var currentCommit: CharSequence

    fun currentCommit(currentCommit: CharSequence) {
        this.currentCommit = currentCommit
    }

    fun currentCommit(currentCommit: Provider<CharSequence>) {
        currentCommitProvider = currentCommit
    }

    fun currentCommit(currentCommit: () -> CharSequence)

    override var lastCommitProvider: Provider<CharSequence>
    override var lastCommit: CharSequence

    fun lastCommit(lastCommit: CharSequence) {
        this.lastCommit = lastCommit
    }

    fun lastCommit(lastCommit: Provider<CharSequence>) {
        lastCommitProvider = lastCommit
    }

    fun lastCommit(lastCommit: () -> CharSequence)

    override var optionsProvider: Provider<Iterable<Any>>
    override var options: Iterable<Any>

    fun options(options: Iterable<Any>) {
        this.options = options
    }

    fun options(options: Provider<Iterable<Any>>) {
        optionsProvider = options
    }

    fun options(options: () -> Iterable<Any>)
}
