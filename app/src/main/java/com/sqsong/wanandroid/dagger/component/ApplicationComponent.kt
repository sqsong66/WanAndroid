package com.sqsong.wanandroid.dagger.component

import android.app.Application
import com.sqsong.wanandroid.base.BaseApplication
import com.sqsong.wanandroid.dagger.module.ActivityBindingModule
import com.sqsong.wanandroid.dagger.module.AppModule
import com.sqsong.wanandroid.dagger.module.CommonModule
import com.sqsong.wanandroid.dagger.module.HttpModule
import dagger.BindsInstance
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@dagger.Component(modules = [AppModule::class, CommonModule::class, HttpModule::class,
    AndroidSupportInjectionModule::class, ActivityBindingModule::class])
interface ApplicationComponent {

    fun inject(application: BaseApplication)

    @dagger.Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

}