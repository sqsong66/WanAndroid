package com.sqsong.wanandroid.base

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import com.sqsong.wanandroid.common.ActivityLifecycleCallbacksImpl
import com.sqsong.wanandroid.dagger.component.DaggerApplicationComponent
import com.sqsong.wanandroid.theme.ThemeOverlayUtil
import com.sqsong.wanandroid.theme.ThemeResourceProvider
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var mInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mPreferences: SharedPreferences

    @Inject
    lateinit var mActivityLifecycleCallback: ActivityLifecycleCallbacksImpl

    @Inject
    lateinit var mActivityList: MutableList<Activity?>

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
       // setupThemeStyle()
        registerActivityLifecycleCallbacks(mActivityLifecycleCallback)
    }

    private fun setupThemeStyle() {
        val provider = ThemeResourceProvider()
        val colorTypedArray = resources.obtainTypedArray(provider.getThemeColors())
        val index: Int = mPreferences[Constants.THEMEOVERLAY_INDEX, 0] ?: 0
        val themeOverlay = colorTypedArray.getResourceId(index, 0)
        ThemeOverlayUtil.mThemeOverlays = themeOverlay
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return mInjector
    }

    open fun quitApp() {
        for (activity in mActivityList) {
            activity?.finish()
        }
    }

    fun getActivityList(): List<Activity?> {
        return mActivityList
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
            private set
    }

}