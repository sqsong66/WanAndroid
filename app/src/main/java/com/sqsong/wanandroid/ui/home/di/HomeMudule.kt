package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.di.scope.FragmentView
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.mvp.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.HomeModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @FragmentView("homeView")
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