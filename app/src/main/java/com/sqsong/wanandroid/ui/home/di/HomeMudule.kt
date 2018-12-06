package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.mvp.home.HomeContract
import dagger.Binds
import dagger.Module

@Module
abstract class HomeModule {

    @Binds
    abstract fun provideHomeView(homeFragment: HomeFragment): HomeContract.HomeView

    /*@FragmentScope
    @Provides
    fun provideHomeModel(apiService: ApiService): HomeModel {
        return HomeModel(apiService)
    }*/

}