package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.ui.home.fragment.KnowledgeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class KnowledgeModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeKnowledgeFragment(): KnowledgeFragment

}