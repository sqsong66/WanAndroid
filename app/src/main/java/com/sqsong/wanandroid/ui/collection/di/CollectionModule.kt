package com.sqsong.wanandroid.ui.collection.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.collection.mvp.CollectionModel
import dagger.Module
import dagger.Provides

@Module
class CollectionModule {

    @ActivityScope
    @Provides
    fun provideCollectionModel(apiService: ApiService): CollectionModel {
        return CollectionModel(apiService)
    }

}