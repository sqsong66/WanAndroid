package com.sqsong.wanandroid.base

import android.app.Application

class BaseApplication : Application() {

    var mActivityList = mutableListOf<BaseActivity>()

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    open fun addActivity(activity: BaseActivity) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity)
        }
    }

    open fun removeActivity(activity: BaseActivity) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity)
        }
    }

    open fun getActivityList(): List<BaseActivity> {
        return mActivityList
    }

    companion object {
        lateinit var INSTANCE: BaseApplication
            private set

    }

}