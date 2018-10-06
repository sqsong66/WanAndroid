package com.sqsong.wanandroid.dagger

import android.app.Application
import com.sqsong.wanandroid.base.BaseApplication
import dagger.BindsInstance
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@dagger.Component(modules = [AndroidSupportInjectionModule::class,
    AppModule::class, HomeModule::class])
interface ApplicationComponent {

    fun inject(application: BaseApplication)

    @dagger.Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder

        fun build(): ApplicationComponent

    }

}