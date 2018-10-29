package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.mvp.navigation.NavigationModel
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {

    @FragmentScope
    @Provides
    fun provideNavigationModel(apiService: ApiService): NavigationModel {
        return NavigationModel(apiService)
    }

}