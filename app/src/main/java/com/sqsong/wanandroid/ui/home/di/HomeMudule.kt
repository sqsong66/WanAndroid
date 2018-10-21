package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.mvp.home.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.home.HomeModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @FragmentScope
    @Provides
    fun provideHomeView(homeFragment: HomeFragment): HomeContract.HomeView {
        return homeFragment
    }

    @FragmentScope
    @Provides
    fun provideHomeModel(apiService: ApiService): HomeModel {
        return HomeModel(apiService)
    }

}