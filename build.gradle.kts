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
    id("groovy")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.9.7"
    id("com.github.johnrengelman.shadow") version "2.0.4"
    id("maven-publish")
    kotlin("jvm") version "1.3.21"
}

group = "com.github.breadmoirai"
version = "2.2.4"

repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    implementation("org.codehaus.groovy:groovy-all:2.5.4")
    compile("com.squareup.okhttp3:okhttp:3.8.1")
    compile("com.j256.simplemagic:simplemagic:1.10")
    compile("org.zeroturnaround:zt-exec:1.10")

    compileOnly("com.gradle.publish:plugin-publish-plugin:0.9.7")

//    testCompile("org.spockframework:spock-core:1.1-groovy-2.4") {
//        exclude(group = "org.codehaus.groovy")
//    }
//    testCompile(group = "org.testfx", name = "testfx-core", version = "4.0.13-alpha")
//    testCompile(group = "org.testfx", name = "testfx-spock", version = "4.0.13-alpha")
//    testCompile(gradleTestKit())
    implementation(kotlin("stdlib-jdk8"))
}

gradlePlugin {
    plugins.create("github-release") {
        id = "com.github.breadmoirai.github-release"
        implementationClass = "com.github.breadmoirai.GithubReleasePlugin"
    }
}

pluginBundle {
    website = "https://github.com/BreadMoirai/github-release-gradle-plugin"
    vcsUrl = "https://github.com/BreadMoirai/github-release-gradle-plugin"
    description = "A Gradle Plugin to send Releases to Github "
    tags = setOf("github", "release")

    plugins.create("github-release") {
        id = "com.github.breadmoirai.github-release"
        displayName = "Github Release Plugin"
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
