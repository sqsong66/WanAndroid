package com.sqsong.wanandroid.network

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

class CookieManager(context: Context) {

    private var cookieJar: PersistentCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

    fun getCookieJar(): PersistentCookieJar {
        return cookieJar
    }

    fun clearCookieInfo() {
        cookieJar.clear()
    }

    companion object {

        private var INSTANCE: CookieManager? = null

        fun getInstance(context: Context): CookieManager {
            return INSTANCE ?: CookieManager(context).apply { INSTANCE = this }
        }
    }

}