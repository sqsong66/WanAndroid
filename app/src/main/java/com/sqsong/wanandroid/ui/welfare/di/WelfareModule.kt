package com.sqsong.wanandroid.ui.welfare.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.welfare.mvp.WelfareModel
import dagger.Module
import dagger.Provides

@Module
class WelfareModule {

    @ActivityScope
    @Provides
    fun provideWelfareModel(apiService: ApiService): WelfareModel {
        return WelfareModel(apiService)
    }

}