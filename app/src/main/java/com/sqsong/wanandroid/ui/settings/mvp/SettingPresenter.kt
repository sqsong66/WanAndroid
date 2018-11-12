package com.sqsong.wanandroid.ui.settings.mvp

import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SettingPresenter @Inject constructor(private val disposable: CompositeDisposable) :
        BasePresenter<SettingContract.View, IModel>(null, disposable) {

    override fun onAttach(view: SettingContract.View) {
        super.onAttach(view)

        disposable.add(languageDisposable())
    }

    private fun languageDisposable(): Disposable {
        return mView.languageClickObservable()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView.showLanguageDialog()
                }
    }

}