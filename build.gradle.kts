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

plugins {
    groovy
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.0"
    id("com.github.johnrengelman.shadow") version "2.0.4"
    `maven-publish`
    kotlin("jvm") version "1.3.21"
    kotlin("kapt") version "1.3.21"
}

group = "com.heinrichreimer"
version = "3.0.0"

repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.codehaus.groovy:groovy-all:2.5.4")
    implementation("com.squareup.okhttp3:okhttp:3.8.1")
    implementation("com.j256.simplemagic:simplemagic:1.10")
    implementation("com.gradle.publish:plugin-publish-plugin:0.9.7")
    implementation("org.zeroturnaround:zt-exec:1.10")

    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.5.0")
    implementation("com.squareup.moshi:moshi:1.8.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    testImplementation(gradleTestKit())
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.1")
    testImplementation("ch.tutteli:tutteli-spek-extensions:0.3.0")

    testImplementation("org.amshove.kluent:kluent:1.48")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.1")
    testRuntimeOnly(kotlin("reflect"))
}

// setup the test task
val test by tasks.getting(Test::class) {
    @Suppress("UnstableApiUsage")
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

sourceSets {
    create("integrationTest") {
        java.srcDir(file("src/integTest/java"))
        resources.srcDir(file("src/integTest/resources"))
        compileClasspath += sourceSets.main.get().output + configurations.testRuntime
        runtimeClasspath += output + compileClasspath
    }
    create("functionalTest") {
        java.srcDir(file("src/funcTest/java"))
        resources.srcDir(file("src/funcTest/resources"))
        compileClasspath += sourceSets.main.get().output + configurations.testRuntime
        runtimeClasspath += output + compileClasspath
    }
}

pluginBundle {
    website = "https://github.com/heinrichreimer/gradle-github-release"
    vcsUrl = "https://github.com/heinrichreimer/gradle-github-release"
    tags = setOf(
            "github",
            "release",
            "continuous integration"
    )
}

gradlePlugin {
    @Suppress("UnstableApiUsage")
    plugins.create("githubRelease") {
        id = "com.heinrichreimer.github-release"
        displayName = "Gradle Github Release Plugin"
        description = "A Gradle Plugin to post releases to GitHub."
        implementationClass = "com.heinrichreimer.gradle.plugin.github.release.GithubReleasePlugin"
    }
}

val createPluginClasspath by tasks.creating {
    val outputDir = file("$buildDir/resources/test")
    val testSourceSet = sourceSets.test.get()

    inputs.files(testSourceSet.runtimeClasspath)
    outputs.dir(outputDir)

    doLast {
        outputDir.mkdirs()
        file("$outputDir/plugin-classpath.txt")
                .writeText(testSourceSet.runtimeClasspath.joinToString(separator = "\n"))
    }
}
val test by tasks.getting {
    dependsOn(createPluginClasspath)
}
