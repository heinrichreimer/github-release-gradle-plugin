/*
 *    Copyright 2017 - 2018 BreadMoirai (Ton Ly), 2018 Jan Heinrich Reimer
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

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

@Suppress("UnstableApiUsage")
class GithubReleaseTask @Inject constructor(
        objects: ObjectFactory,
        providers: ProviderFactory,
        files: ProjectLayout
) : DefaultTask(), MutableGithubReleaseConfiguration by GithubReleaseExtension(objects, files, providers) {

    init {
        group = "publishing"
    }

    @TaskAction
    fun publishRelease() {
        GithubRelease(this).run()
    }
}
