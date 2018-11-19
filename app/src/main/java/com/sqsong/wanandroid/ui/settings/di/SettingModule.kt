package com.sqsong.wanandroid.ui.settings.di

import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.ui.settings.dialog.LanguageSettingDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeThemeSwitcherDialog(): ThemeSwitcherDialog

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeLanguageDialog(): LanguageSettingDialog

}