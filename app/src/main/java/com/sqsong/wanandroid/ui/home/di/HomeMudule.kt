package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.mvp.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.HomeModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeModule {

    @FragmentScope
    @ContributesAndroidInjector()
    abstract fun contributeHomeFragment(): HomeFragment

    @FragmentScope
    @Binds
    abstract fun provideHomeView(homeView: HomeFragment): HomeContract.HomeView

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideHomeModel(apiService: ApiService): HomeModel {
            return HomeModel(apiService)
        }
    }

}