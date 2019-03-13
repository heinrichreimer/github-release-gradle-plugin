package com.heinrichreimer.gradle.plugin.github.release

import org.jetbrains.spek.api.lifecycle.GroupScope as OldGroupScope
import org.jetbrains.spek.api.lifecycle.LifecycleListener as OldLifecycleListener
import org.jetbrains.spek.api.lifecycle.TestScope as OldTestScope
import org.spekframework.spek2.lifecycle.GroupScope as NewGroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener as NewLifecycleListener
import org.spekframework.spek2.lifecycle.TestScope as NewTestScope

fun NewGroupScope.toOldApi(): OldGroupScope = object : OldGroupScope {
    override val parent: OldGroupScope?
        get() = this@toOldApi.parent?.toOldApi()
}

fun OldGroupScope.toNewApi(): NewGroupScope = object : NewGroupScope {
    override val parent: NewGroupScope?
        get() = this@toNewApi.parent?.toNewApi()
}

fun NewTestScope.toOldApi(): OldTestScope = object : OldTestScope {
    override val parent: OldGroupScope
        get() = this@toOldApi.parent.toOldApi()
}

fun OldTestScope.toNewApi(): NewTestScope = object : NewTestScope {
    override val parent: NewGroupScope
        get() = this@toNewApi.parent.toNewApi()
}

fun NewLifecycleListener.toOldApi(): OldLifecycleListener = object : OldLifecycleListener {
    override fun afterExecuteGroup(group: OldGroupScope) {
        this@toOldApi.afterExecuteGroup(group.toNewApi())
    }

    override fun afterExecuteTest(test: OldTestScope) {
        this@toOldApi.afterExecuteTest(test.toNewApi())
    }

    override fun beforeExecuteGroup(group: OldGroupScope) {
        this@toOldApi.beforeExecuteGroup(group.toNewApi())
    }

    override fun beforeExecuteTest(test: OldTestScope) {
        this@toOldApi.beforeExecuteTest(test.toNewApi())
    }
}

fun OldLifecycleListener.toNewApi(): NewLifecycleListener = object : NewLifecycleListener {
    override fun afterExecuteGroup(group: NewGroupScope) {
        this@toNewApi.afterExecuteGroup(group.toOldApi())
    }

    override fun afterExecuteTest(test: NewTestScope) {
        this@toNewApi.afterExecuteTest(test.toOldApi())
    }

    override fun beforeExecuteGroup(group: NewGroupScope) {
        this@toNewApi.beforeExecuteGroup(group.toOldApi())
    }

    override fun beforeExecuteTest(test: NewTestScope) {
        this@toNewApi.beforeExecuteTest(test.toOldApi())
    }
}