package com.sqsong.wanandroid.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sqsong.wanandroid.base.BaseApplication

class PreferenceHelper(context: Context) {

    private var preference: SharedPreferences = defaultPrefs(context)

    private fun defaultPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    open fun putValue(key: String, value: Any?) {
        preference[key] = value
    }

    open fun getValue(key: String, defaultValue: Any?): Any? {
        return preference[key]
    }


    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> {
                val typeName = value!!::class.simpleName
                throw UnsupportedOperationException("Not support the $typeName type")
            }
        }
    }

    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> {
                val typeName = T::class.simpleName
                throw UnsupportedOperationException("Not support the $typeName type")
            }
        }
    }

    companion object {
        private lateinit var INSTANCE: PreferenceHelper

        fun getInstance(): PreferenceHelper {
            if (INSTANCE == null) {
                INSTANCE = PreferenceHelper(BaseApplication.INSTANCE)
            }
            return INSTANCE
        }
    }

}