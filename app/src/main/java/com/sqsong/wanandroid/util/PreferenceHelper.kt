package com.sqsong.wanandroid.util

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sqsong.wanandroid.base.BaseApplication
import kotlin.reflect.KProperty

class PreferenceHelper<T>(val key: String, val defaultValue: T) {

    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(BaseApplication.INSTANCE.applicationContext)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValueToPrefs(key, value)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValueFromPrefs(key, defaultValue)
    }

    private fun putValueToPrefs(key: String, value: T) = with(prefs.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw UnsupportedOperationException("Not support this type.")
        }.apply()
    }

    private fun getValueFromPrefs(key: String, defaultValue: T): T = with(prefs) {
        val result: Any = when (defaultValue) {
            is String -> getString(key, defaultValue)
            is Int -> getInt(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            is Long -> getLong(key, defaultValue)
            is Boolean -> getBoolean(key, defaultValue)
            else -> throw UnsupportedOperationException("Not support this type.")
        }
        return result as T
    }

}

