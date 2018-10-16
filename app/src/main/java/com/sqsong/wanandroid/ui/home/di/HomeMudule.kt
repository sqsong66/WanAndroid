package com.sqsong.wanandroid.ui.home.di

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.mvp.HomeModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @FragmentScope
    @Provides
    fun provideHomeModel(apiService: ApiService): HomeModel {
        return HomeModel(apiService)
    }

    @FragmentScope
    @Provides
    fun provideLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }

}