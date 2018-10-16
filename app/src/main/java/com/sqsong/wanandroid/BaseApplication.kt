package com.sqsong.wanandroid

import android.app.Activity
import com.sqsong.wanandroid.common.ActivityLifecycleCallbacksImpl
import com.sqsong.wanandroid.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class BaseApplication : DaggerApplication() {

    @Inject
    lateinit var mActivityLifecycleCallback: ActivityLifecycleCallbacksImpl

    @Inject
    lateinit var mActivityList: MutableList<Activity?>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        INSTANCE = this
        registerActivityLifecycleCallbacks(mActivityLifecycleCallback)
    }

    fun quitApp() {
        for (activity in mActivityList) {
            activity?.finish()
        }
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
            private set
    }

}