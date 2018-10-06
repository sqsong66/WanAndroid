package com.sqsong.wanandroid.base

import android.app.Activity
import android.app.Application
import com.sqsong.wanandroid.common.ActivityLifecycleCallbacksImpl
import com.sqsong.wanandroid.dagger.DaggerApplicationComponent
import com.sqsong.wanandroid.theme.ThemeOverlayUtil
import com.sqsong.wanandroid.theme.ThemeResourceProvider
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mActivityLifecycleCallback: ActivityLifecycleCallbacksImpl

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this)
        init()
    }

    private fun init() {
        setupThemeStyle()
        registerActivityLifecycleCallbacks(mActivityLifecycleCallback)
    }

    private fun setupThemeStyle() {
        val provider = ThemeResourceProvider()
        val colorTypedArray = resources.obtainTypedArray(provider.getThemeColors())
        var index by PreferenceHelper(Constants.THEMEOVERLAY_INDEX, 0)
        val themeOverlay = colorTypedArray.getResourceId(index, 0)
        ThemeOverlayUtil.mThemeOverlays = themeOverlay
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return injector
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