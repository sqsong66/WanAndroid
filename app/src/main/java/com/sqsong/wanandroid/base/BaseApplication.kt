package com.sqsong.wanandroid.base

import android.app.Activity
import android.app.Application
import com.sqsong.wanandroid.common.ActivityLifecycleCallbackImpl
import com.sqsong.wanandroid.theme.ThemeOverlayUtil
import com.sqsong.wanandroid.theme.ThemeResourceProvider
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper

class BaseApplication : Application() {

    private lateinit var mActivityLifecycleCallback: ActivityLifecycleCallbackImpl

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        setupThemeStyle()
        mActivityLifecycleCallback = ActivityLifecycleCallbackImpl()
        registerActivityLifecycleCallbacks(mActivityLifecycleCallback)
    }

    private fun setupThemeStyle() {
        val provider = ThemeResourceProvider()
        val colorTypedArray = resources.obtainTypedArray(provider.getThemeColors())
        var index by PreferenceHelper(Constants.THEMEOVERLAY_INDEX, 0)
        val themeOverlay = colorTypedArray.getResourceId(index, 0)
        ThemeOverlayUtil.mThemeOverlays = themeOverlay
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