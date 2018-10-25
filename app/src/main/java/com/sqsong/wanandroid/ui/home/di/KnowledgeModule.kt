package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.mvp.knowledge.KnowledgeModel
import dagger.Module
import dagger.Provides

@Module
class KnowledgeModule {

    @FragmentScope
    @Provides
    fun provideKnowledgeModel(apiService: ApiService): KnowledgeModel {
        return KnowledgeModel(apiService)
    }

}