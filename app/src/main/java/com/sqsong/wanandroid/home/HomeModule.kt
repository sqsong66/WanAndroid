package com.sqsong.wanandroid.home

import com.sqsong.wanandroid.dagger.scope.FragmentScope
import com.sqsong.wanandroid.theme.ThemeResourceProvider
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideThemeResourceProvider(): ThemeResourceProvider {
            return ThemeResourceProvider()
        }
    }

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeThemeSwitcherDialog(): ThemeSwitcherDialog

}