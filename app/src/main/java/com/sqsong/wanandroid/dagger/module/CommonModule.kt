package com.sqsong.wanandroid.dagger.module

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.sqsong.wanandroid.theme.ThemeResourceProvider
import com.sqsong.wanandroid.theme.ThemeSwitcherManager
import com.sqsong.wanandroid.util.PreferenceHelper
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class CommonModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Singleton
    @Provides
    fun provideActivityList(): List<Activity?> {
        return mutableListOf()
    }

    @Singleton
    @Provides
    fun providePreferences(context: Context): SharedPreferences {
        return PreferenceHelper.defaultPrefs(context)
    }

    @Singleton
    @Provides
    fun provideThemeResourceProvider(): ThemeResourceProvider {
        return ThemeResourceProvider()
    }

    @Singleton
    @Provides
    fun provideThemeSwitcherManager(context: Context,
                                    sourceProvider: ThemeResourceProvider,
                                    preferences: SharedPreferences,
                                    activityList: MutableList<Activity?>): ThemeSwitcherManager {
        return ThemeSwitcherManager(context, sourceProvider, preferences, activityList)
    }

}