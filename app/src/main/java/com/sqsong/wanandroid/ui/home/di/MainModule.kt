package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.fragment.KnowledgeFragment
import com.sqsong.wanandroid.ui.home.fragment.NavigationFragment
import com.sqsong.wanandroid.ui.home.fragment.ProjectFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [HomeModule::class, KnowledgeModule::class, NavigationModule::class, ProjectModule::class])
abstract class MainModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeThemeSwitcherDialog(): ThemeSwitcherDialog

}