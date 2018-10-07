package com.sqsong.wanandroid.dagger.module

import com.sqsong.wanandroid.dagger.scope.ActivityScope
import com.sqsong.wanandroid.home.HomeActivity
import com.sqsong.wanandroid.home.HomeModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeActivity(): HomeActivity

}