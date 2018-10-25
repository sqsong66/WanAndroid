package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.ui.home.activity.MainActivity
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.fragment.KnowledgeFragment
import com.sqsong.wanandroid.ui.home.fragment.NavigationFragment
import com.sqsong.wanandroid.ui.home.fragment.ProjectFragment
import com.sqsong.wanandroid.ui.home.mvp.MainContract
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector/*(modules = [KnowledgeModule::class])*/
    abstract fun contributeKnowledgeFragment(): KnowledgeFragment

    @ContributesAndroidInjector/*(modules = [NavigationModule::class])*/
    abstract fun contributeNavigationFragment(): NavigationFragment

    @ContributesAndroidInjector/*(modules = [ProjectModule::class])*/
    abstract fun contributeProjectFragment(): ProjectFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeThemeSwitcherDialog(): ThemeSwitcherDialog

    @ActivityScope
    @Binds
    abstract fun provideMainView(activity: MainActivity): MainContract.View

}