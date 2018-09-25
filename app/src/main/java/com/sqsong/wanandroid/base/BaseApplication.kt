package com.sqsong.wanandroid.base

import android.app.Activity
import android.app.Application
import com.sqsong.wanandroid.common.ActivityLifecycleCallbackImpl

class BaseApplication : Application() {

    private lateinit var mActivityLifecycleCallback: ActivityLifecycleCallbackImpl

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        mActivityLifecycleCallback = ActivityLifecycleCallbackImpl()
        registerActivityLifecycleCallbacks(mActivityLifecycleCallback)
    }

    open fun getActivityList(): List<Activity?> {
        return mActivityLifecycleCallback.getActivityList()
    }

    open fun quitApp() {
        for (activity in getActivityList()) {
            activity?.finish()
        }
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
            private set
    }

}