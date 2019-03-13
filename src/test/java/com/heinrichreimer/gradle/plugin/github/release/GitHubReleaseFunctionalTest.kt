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

import ch.tutteli.spek.extensions.TempFolder
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should not be null`
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File

object GitHubReleaseFunctionalTest : Spek({

    val testProjectDir = TempFolder.perTest() //or perAction() or perGroup()
    registerListener(testProjectDir.toNewApi())

    val buildFile: File by memoized {
        testProjectDir.newFile("build.gradle").toFile()
    }
    val pluginClasspath by memoized {
        val pluginClasspathResource = this::class.java.classLoader.getResource("plugin-classpath.txt")
                ?: throw IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")

        pluginClasspathResource
                .file
                .lineSequence()
                .map { File(it) }
                .toList()
    }

    describe("manual test") {

        lateinit var result: BuildResult

        beforeEach {
            buildFile.writeText(
                    """
                    plugins {
                        id 'com.heinrichreimer.github-release'
                    }

                    group = 'com.heinrichreimer'
                    version = 'test'

                    githubRelease {
                        repository 'gradle-github-release'
                        body changelog {}
                    }

                    """.trimIndent()
            )

            result = GradleRunner.create()
                    .withProjectDir(File("C:\\Users\\TonTL\\Desktop\\Git\\BreadBotFramework"))
                    .withArguments("githubRelease", "--info", "--stacktrace")
                    .withPluginClasspath(pluginClasspath)
                    .build()
        }

        it("build result should succeed") {
            val gradleReleaseTask = result.task(":githubRelease")
            gradleReleaseTask.`should not be null`()

            val outcome = gradleReleaseTask.outcome
            outcome `should be` TaskOutcome.SUCCESS

            println("result.output = ${result.output}")
        }
    }

    describe("manual login test") {
        //        when:
//        //new GithubLoginApp().awaitResult()
//
//                /*
//                GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(25, 25, 25, 25));
//
//        Text scenetitle = new Text("Login to Github");
//        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//        grid.add(scenetitle, 0, 0, 1, 1);
//
//        Label usernameLabel = new Label("User Name:");
//        grid.add(usernameLabel, 0, 1);
//
//        TextField usernameField = new TextField();
//        usernameField.setId("field-username");
//        grid.add(usernameField, 1, 1);
//
//        Label passwordLabel = new Label("Password:");
//        grid.add(passwordLabel, 0, 2);
//
//        PasswordField passwordField = new PasswordField();
//        passwordField.setId("field-password");
//        grid.add(passwordField, 1, 2);
//
//        Button loginButton = new Button("Sign in");
//        loginButton.setId("button-login");
//        loginButton.setDisable(true);
//        loginButton.setDefaultButton(true);
//        Button cancelButton = new Button("Cancel");
//        cancelButton.setId("button-cancel");
//        HBox buttonBox = new HBox(15);
//        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
//        buttonBox.getChildren().addAll(cancelButton, loginButton);
//        grid.add(buttonBox, 0, 4, 2, 1);
//
//
//        usernameField.textProperty().addListener(getValidationListener(passwordField.textProperty(), loginButton));
//        passwordField.textProperty().addListener(getValidationListener(usernameField.textProperty(), loginButton));
//
//        loginButton.setOnAction(e -> {
//            final String username = usernameField.getText();
//            final String password = passwordField.getText();
//            final String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
//            future.complete(Optional.of(credentials));
//            primaryStage.close();
//        });
//
//        cancelButton.setOnAction(e -> {
//            future.complete(Optional.empty());
//            primaryStage.close();
//        });
//
//        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (KeyCode.ESCAPE == event.getCode()) {
//                future.complete(Optional.empty());
//                primaryStage.close();
//            }
//        });
//
//        Scene scene = new Scene(grid, 300, 275);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        primaryStage.toFront();
//                 */
//
//
//        then:
//        true
    }
})