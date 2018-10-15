package com.sqsong.wanandroid.ui.home.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.ui.home.fragment.NavigationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NavigationModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNavigationFragment(): NavigationFragment

}