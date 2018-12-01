package com.sqsong.wanandroid.common.language

import android.content.Context
import android.content.SharedPreferences
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.PreferenceHelper.set
import java.util.*
import javax.inject.Inject

class LanguageManager @Inject constructor(private val preferences: SharedPreferences) {

    /**
     * @param languageType 0 - follow system,  1 - English,  2 - Chinese
     */
    fun changeLanguage(context: Context, languageType: Int?) {
        val configuration = context.resources.configuration
        var locale: Locale = when (languageType) {
            Constants.LANGUAGE_TYPE_ENGLISH -> Locale.ENGLISH
            Constants.LANGUAGE_TYPE_CHINESE -> Locale.SIMPLIFIED_CHINESE
            Constants.LANGUAGE_TYPE_TRADITION_CHINESE -> Locale.TRADITIONAL_CHINESE
            else -> Locale.getDefault()
        }

        configuration.locale = locale
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        preferences[Constants.LANGUAGE_TYPE] = languageType

        /*when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                configuration.setLocale(locale)
                val localeList = LocaleList(locale)
                LocaleList.setDefault(localeList)
                context.createConfigurationContext(configuration)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                configuration.setLocale(locale)
                context.createConfigurationContext(configuration)
            }
            else -> {
                configuration.locale = locale
                context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
            }
        }*/
    }

    fun updateLanguageConfiguration(context: Context) {
        val type = preferences[Constants.LANGUAGE_TYPE, 0]
        changeLanguage(context, type)
    }
}