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

package com.github.breadmoirai.configuration

fun GithubReleaseConfiguration.copyTo(configuration: MutableGithubReleaseConfiguration) {
    configuration.ownerProvider = ownerProvider
    configuration.repoProvider = repoProvider
    configuration.authorizationProvider = authorizationProvider
    configuration.tagNameProvider = tagNameProvider
    configuration.targetCommitishProvider = targetCommitishProvider
    configuration.releaseNameProvider = releaseNameProvider
    configuration.bodyProvider = bodyProvider
    configuration.draftProvider = draftProvider
    configuration.prereleaseProvider = prereleaseProvider
    configuration.releaseAssets = releaseAssets
    configuration.overwriteProvider = overwriteProvider
    configuration.allowUploadToExistingProvider = allowUploadToExistingProvider
}

fun MutableGithubReleaseConfiguration.copyFrom(configuration: GithubReleaseConfiguration) =
        configuration.copyTo(this)
