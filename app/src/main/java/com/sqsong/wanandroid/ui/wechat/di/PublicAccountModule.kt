package com.sqsong.wanandroid.ui.wechat.di

import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.di.scope.FragmentScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.wechat.fragment.PublicAccountFragment
import com.sqsong.wanandroid.ui.wechat.mvp.PublicAccountContract
import com.sqsong.wanandroid.ui.wechat.mvp.PublicAccountModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class PublicAccountModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributePublicAccountFragment(): PublicAccountFragment

    @Module
    companion object {
        @ActivityScope
        @Provides
        fun providePublicAccountModel(apiService: ApiService): PublicAccountContract.Model {
            return PublicAccountModel(apiService)
        }
    }

}