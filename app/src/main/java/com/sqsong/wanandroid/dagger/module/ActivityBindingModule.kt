package com.sqsong.wanandroid.dagger.module

import com.sqsong.wanandroid.dagger.scope.ActivityScope
import com.sqsong.wanandroid.home.HomeActivity
import com.sqsong.wanandroid.home.HomeModule
import com.sqsong.wanandroid.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeLoginActivity(): LoginActivity

}