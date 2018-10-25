package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.activity.KnowledgeActivity
import com.sqsong.wanandroid.ui.home.mvp.knowledge.KnowledgeModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class KnowledgeModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ChildKnowledgeModule::class])
    abstract fun contributeKnowledgeActivity(): KnowledgeActivity

    @Module
    companion object {
        @FragmentScope
        @Provides
        fun provideKnowledgeModel(apiService: ApiService): KnowledgeModel {
            return KnowledgeModel(apiService)
        }
    }

}