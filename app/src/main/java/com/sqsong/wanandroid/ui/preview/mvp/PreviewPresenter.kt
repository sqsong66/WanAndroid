package com.sqsong.wanandroid.ui.preview.mvp

import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class PreviewPresenter constructor(private val disposable: CompositeDisposable) :
        BasePresenter<PreviewContract.View, IModel>(null, disposable) {

    override fun onAttach(view: PreviewContract.View) {
        super.onAttach(view)
        disposable.add(imageClickDisposable())
    }

    private fun imageClickDisposable(): Disposable {
        return mView.imageClickObservable()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView.finishActivity()
                }
    }

}