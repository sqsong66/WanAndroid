package com.sqsong.wanandroid.ui.login.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.login.RegisterActivity
import com.sqsong.wanandroid.ui.login.mvp.RegisterContract
import com.sqsong.wanandroid.ui.login.mvp.RegisterModel
import dagger.Module
import dagger.Provides

@Module
class RegisterModule {

    @ActivityScope
    @Provides
    fun provideRegisterView(activity: RegisterActivity): RegisterContract.View {
        return activity
    }

    @ActivityScope
    @Provides
    fun provideRegisterModel(apiService: ApiService): RegisterModel {
        return RegisterModel(apiService)
    }

}