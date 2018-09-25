package com.sqsong.wanandroid.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.theme.ThemeOverlayUtil

class ActivityLifecycleCallbackImpl : Application.ActivityLifecycleCallbacks {

    private var mActivityList = mutableListOf<Activity?>()

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity)
        }

        // 设置主题
        if ((activity?.javaClass?.isAnnotationPresent(ChangeThemeAnnotation::class.java))!!)
            ThemeOverlayUtil.applyThemeOverlay(activity)

        // 处理Activity事件
        if (activity is IAppCompatActivity) {
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

    open fun getActivityList(): List<Activity?> {
        return mActivityList
    }
}