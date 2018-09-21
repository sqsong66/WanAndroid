package com.sqsong.wanandroid.base

import android.app.Application

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
            private set
    }

}