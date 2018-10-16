package com.sqsong.wanandroid.di.component

import android.app.Application
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.di.module.ActivityBindingModule
import com.sqsong.wanandroid.di.module.AppModule
import com.sqsong.wanandroid.di.module.CommonModule
import com.sqsong.wanandroid.di.module.HttpModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CommonModule::class, HttpModule::class,
    AndroidSupportInjectionModule::class, ActivityBindingModule::class])
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @dagger.Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

}