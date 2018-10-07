package com.sqsong.wanandroid.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.sqsong.wanandroid.common.ActivityLifecycleCallbacksImpl
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
    fun provideActivityLifecycleCallbacks(): ActivityLifecycleCallbacksImpl {
        return ActivityLifecycleCallbacksImpl()
    }

    @Singleton
    @Provides
    fun providePreferences(context: Context): SharedPreferences {
        return PreferenceHelper.defaultPrefs(context)
    }

}