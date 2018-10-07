package com.sqsong.wanandroid.home

import com.sqsong.wanandroid.dagger.scope.ActivityScope
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @ActivityScope
    @Provides
    fun provideThemeSwitcherDialog(): ThemeSwitcherDialog {
        return ThemeSwitcherDialog()
    }

}