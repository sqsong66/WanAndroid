package com.sqsong.wanandroid.dagger

import com.sqsong.wanandroid.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

}