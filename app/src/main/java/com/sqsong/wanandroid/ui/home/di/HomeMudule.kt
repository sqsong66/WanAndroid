package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.mvp.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.HomeModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
class HomeModule {

//    @Binds
//    abstract fun provideHomeView(homeView: HomeFragment): HomeContract.HomeView



    @FragmentScope
    @Provides
    fun provideHomeModel(apiService: ApiService): HomeModel {
        return HomeModel(apiService)
    }

//    @Provides
//    fun provideHomeView(homeFragment: HomeFragment): HomeContract.HomeView {
//        return homeFragment
//    }

}