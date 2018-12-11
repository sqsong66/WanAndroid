package com.sqsong.wanandroid.common

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.common.inter.TranslucentNavigation
import com.sqsong.wanandroid.common.language.LanguageManager
import com.sqsong.wanandroid.theme.ThemeSwitcherManager
import me.jessyan.autosize.AutoSize
import javax.inject.Inject

class ActivityLifecycleCallbacksImpl @Inject constructor() : Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var mActivityList: MutableList<Activity?>

    @Inject
    lateinit var mThemeManager: ThemeSwitcherManager

    @Inject
    lateinit var mLanguageManager: LanguageManager

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity)
        }

        // 设置主题
        mThemeManager.applyThemeOverlay(activity)
        if ((activity?.javaClass?.isAnnotationPresent(TranslucentNavigation::class.java)) == true) {
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        // 设置语言
        mLanguageManager.updateLanguageConfiguration(activity)
        // 由于在设置语言中使用了displayMetrics属性，导致框架里面的屏幕适配失效，需要重新设定activity的density
        AutoSize.autoConvertDensityOfGlobal(activity)

        // 处理Activity事件
        if (activity is IAppCompatActivity) {
            activity.beforeInflateView()
            activity.setContentView(activity.getLayoutResId())
            activity.initEvent()
        }

    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity)
        }
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

}