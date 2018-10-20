package com.sqsong.wanandroid.ui.login.di

import android.content.Context
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.LoadingProgressDialog
import com.sqsong.wanandroid.di.scope.ActivityScope
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.ui.login.mvp.LoginContract
import com.sqsong.wanandroid.ui.login.mvp.LoginModel
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class LoginModule {

    @ActivityScope
    @Binds
    abstract fun provideLoginView(activity: LoginActivity): LoginContract.View

    @Module
    companion object {
        @ActivityScope
        @Provides
        fun provideLoadingDialog(context: Context): LoadingProgressDialog {
            return LoadingProgressDialog.newInstance(context.getString(R.string.text_on_login))
        }

        @ActivityScope
        @Provides
        fun provideLoginModel(apiService: ApiService): LoginModel {
            return LoginModel(apiService)
        }
    }
}