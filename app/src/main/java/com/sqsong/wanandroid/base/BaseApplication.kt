package com.sqsong.wanandroid.base

import android.app.Activity
import android.app.Application
import com.sqsong.wanandroid.common.ActivityLifecycleCallbacksImpl
import com.sqsong.wanandroid.dagger.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var mInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var mActivityLifecycleCallback: ActivityLifecycleCallbacksImpl

    @Inject
    lateinit var mActivityList: MutableList<Activity?>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        init()
    }

    private fun init() {
        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallback)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return mInjector
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