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
