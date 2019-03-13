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

package com.github.breadmoirai

import com.github.breadmoirai.configuration.MutableChangeLogSupplierConfiguration
import com.github.breadmoirai.util.property
import com.github.breadmoirai.util.providerDelegate
import com.github.breadmoirai.util.valueDelegate
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Internal

@Suppress("UnstableApiUsage")
class ChangeLogSupplierExtension(
        objects: ObjectFactory,
        private val providers: ProviderFactory
) : MutableChangeLogSupplierConfiguration {

    constructor(project: Project) : this(
            project.objects,
            project.providers
    )

    @get:Internal
    internal val executableProperty: Property<CharSequence> = objects.property()
    override var executableProvider by executableProperty.providerDelegate
    override var executable by executableProperty.valueDelegate
    override fun executable(executable: () -> CharSequence) {
        executableProvider = providers.provider { executable() }
    }

    @get:Internal
    internal val currentCommitProperty: Property<CharSequence> = objects.property()
    override var currentCommitProvider by currentCommitProperty.providerDelegate
    override var currentCommit by currentCommitProperty.valueDelegate
    override fun currentCommit(currentCommit: () -> CharSequence) {
        currentCommitProvider = providers.provider { currentCommit() }
    }

    @get:Internal
    internal val lastCommitProperty: Property<CharSequence> = objects.property()
    override var lastCommitProvider by lastCommitProperty.providerDelegate
    override var lastCommit by lastCommitProperty.valueDelegate
    override fun lastCommit(lastCommit: () -> CharSequence) {
        lastCommitProvider = providers.provider { lastCommit() }
    }

    @get:Internal
    internal val optionsProperty: Property<Iterable<Any>> = objects.property()
    override var optionsProvider by optionsProperty.providerDelegate
    override var options by optionsProperty.valueDelegate
    override fun options(options: () -> Iterable<Any>) {
        optionsProvider = providers.provider { options() }
    }
}