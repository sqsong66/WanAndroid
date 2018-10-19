package com.sqsong.wanandroid.ui.login.mvp

import com.sqsong.wanandroid.data.BaseBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface RegisterContract {

    interface View : IView {
        fun backDisposable(): Disposable
        fun userNameDisposable(): Disposable
        fun passwordDisposable(): Disposable
        fun confirmPasswordDisposable(): Disposable
    }

    interface Model : IModel {
        fun register(userName: String, password: String, rePassword: String): Observable<BaseBean<*>>
    }
}