package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [KnowledgeModule::class, NavigationModule::class, ProjectModule::class])
abstract class MainModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeThemeSwitcherDialog(): ThemeSwitcherDialog

}