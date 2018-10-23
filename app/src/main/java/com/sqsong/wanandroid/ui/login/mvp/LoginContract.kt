package com.sqsong.wanandroid.ui.login.mvp

import android.widget.TextView
import com.sqsong.wanandroid.data.LoginBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface LoginContract {

    interface View : IView {
        fun getTitleText(): TextView
        fun closeDisposable(): Disposable
        fun userNameDisposable(): Disposable
        fun passwordDisposable(): Disposable
        fun commitObservable(): Observable<Any>
        fun infalteUserName(userName: String)
        fun userNameText(): String
        fun passwordText(): String
        /**
         * @param type 0 - name input layout type; 1 - password input layout type; 2 - password length type;
         * @param errorMsg error message tips.
         */
        fun showInputLayoutError(type: Int, errorMsg: String)

        fun showProcessDialog()
        fun hideProcessDialog()
        fun startHomeActivity()
    }

    interface Model : IModel {
        fun login(userName: String, password: String): Observable<LoginBean>
    }
}