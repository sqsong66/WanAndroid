package com.sqsong.wanandroid.home

import com.sqsong.wanandroid.dagger.scope.FragmentScope
import com.sqsong.wanandroid.home.fragment.HomeFragment
import com.sqsong.wanandroid.home.fragment.KnowledgeFragment
import com.sqsong.wanandroid.home.fragment.NavigationFragment
import com.sqsong.wanandroid.home.fragment.ProjectFragment
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeThemeSwitcherDialog(): ThemeSwitcherDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeHomeKnowledgeFragment(): KnowledgeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeHomeNavigationFragment(): NavigationFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeHomeProjectFragment(): ProjectFragment

}