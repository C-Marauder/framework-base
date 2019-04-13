package com.xqy.androidx.framework.prefers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

class AppPreference<T>(application: Application, private val default: T?) {

    private val name: String = application.packageName

    private val prefers: SharedPreferences by lazy {
        application.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences(name, default)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharedPreferences(name, value)
    }

    private fun putSharedPreferences(name: String, value: T) = with(prefers.edit()) {
        when (value) {
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Float -> putFloat(name, value)
            is Boolean -> putBoolean(name, value)
            is Long -> putLong(name, value)
            else -> {
                throw Exception("value is illegal")
            }
        }
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    private fun getSharedPreferences(name: String, default: T?): T = with(prefers) {
        return when (default) {
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Float -> getFloat(name, default)
            is Boolean -> getBoolean(name, default)
            is Long -> getLong(name, default)
            else -> throw Exception("value is illegal")
        } as T
    }
}