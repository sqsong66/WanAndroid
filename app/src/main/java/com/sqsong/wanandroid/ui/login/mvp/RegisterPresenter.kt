package com.sqsong.wanandroid.ui.login.mvp

import com.sqsong.wanandroid.mvp.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RegisterPresenter @Inject constructor(private val registerView: RegisterContract.View,
                                            private val registerModel: RegisterModel,
                                            private val disposable: CompositeDisposable) :
        BasePresenter<RegisterContract.View, RegisterContract.Model>(registerModel, disposable) {

    override fun onAttach(view: RegisterContract.View) {
        mView = registerView
        registerEvents()
    }

    private fun registerEvents() {
        disposable.add(mView.backDisposable())
        disposable.add(mView.userNameDisposable())
        disposable.add(mView.passwordDisposable())
        disposable.add(mView.confirmPasswordDisposable())
    }

}