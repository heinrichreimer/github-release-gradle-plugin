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

@file:Suppress("UnstableApiUsage")

package com.github.breadmoirai.util

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PropertyProviderDelegate<T>(private val source: Property<T>) : ReadWriteProperty<Any, Provider<T>> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Provider<T> = source
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Provider<T>) = source.set(value)
}

val <T> Property<T>.providerDelegate get() = PropertyProviderDelegate(this)

class PropertyValueDelegate<T>(private val source: Property<T>) : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T = source.get()
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = source.set(value)
}

val <T> Property<T>.valueDelegate get() = PropertyValueDelegate(this)

inline fun <reified T : Any> ObjectFactory.property(): Property<T> = property(T::class.java)

class ConfigurableFileCollectionCollectionDelegate(
        private val source: ConfigurableFileCollection
) : ReadWriteProperty<Any, FileCollection> {
    override fun getValue(thisRef: Any, property: KProperty<*>): FileCollection = source
    override fun setValue(thisRef: Any, property: KProperty<*>, value: FileCollection) = source.setFrom(value as Any)
}

val ConfigurableFileCollection.collectionDelegate
    get() = ConfigurableFileCollectionCollectionDelegate(this)

class ConfigurableFileCollectionFilesDelegate(
        private val source: ConfigurableFileCollection
) : ReadWriteProperty<Any, Iterable<File>> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Iterable<File> = source
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Iterable<File>) = source.setFrom(value)
}

val ConfigurableFileCollection.filesDelegate
    get() = ConfigurableFileCollectionFilesDelegate(this)

class ConfigurableFileCollectionFileDelegate(
        private val source: ConfigurableFileCollection
) : ReadWriteProperty<Any, File> {
    override fun getValue(thisRef: Any, property: KProperty<*>): File = source.singleFile
    override fun setValue(thisRef: Any, property: KProperty<*>, value: File) = source.setFrom(value)
}

val ConfigurableFileCollection.fileDelegate
    get() = ConfigurableFileCollectionFileDelegate(this)
