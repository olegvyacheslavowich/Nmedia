package ru.netology.nmedia.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object NewPostArg : ReadWriteProperty<Bundle, Boolean> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Boolean) {
        thisRef.putBoolean(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Boolean =
        thisRef.getBoolean(property.name)
}