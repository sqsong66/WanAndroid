package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.mvp.knowledge.ChildKnowledgeModel
import dagger.Module
import dagger.Provides

@Module
class ChildKnowledgeModule {

    @ActivityScope
    @Provides
    fun provideChildKnowledgeModel(apiService: ApiService): ChildKnowledgeModel {
        return ChildKnowledgeModel(apiService)
    }

}