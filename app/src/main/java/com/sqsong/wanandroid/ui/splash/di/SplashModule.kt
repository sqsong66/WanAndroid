package com.sqsong.wanandroid.ui.splash.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.ui.splash.SplashActivity
import com.sqsong.wanandroid.ui.splash.mvp.SplashContract
import dagger.Binds
import dagger.Module

@Module
abstract class SplashModule {

    @ActivityScope
    @Binds
    abstract fun provideSplashView(activity: SplashActivity): SplashContract.View

}