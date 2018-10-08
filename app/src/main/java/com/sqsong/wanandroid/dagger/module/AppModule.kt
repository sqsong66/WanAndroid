package com.sqsong.wanandroid.dagger.module

import android.app.Application
import android.content.Context
import com.sqsong.wanandroid.common.ActivityLifecycleCallbacksImpl
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context

    @Binds
    abstract fun proviceActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacksImpl): Application.ActivityLifecycleCallbacks

}