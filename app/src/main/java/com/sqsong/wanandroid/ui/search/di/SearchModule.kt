package com.sqsong.wanandroid.ui.search.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.search.mvp.SearchModel
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @ActivityScope
    @Provides
    fun provideSearchModel(apiService: ApiService): SearchModel {
        return SearchModel(apiService)
    }

}