package com.sqsong.wanandroid.ui.preview.mvp

import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class PreviewPresenter constructor(private val disposable: CompositeDisposable) :
        BasePresenter<PreviewContract.View, IModel>(null) {

    override fun onAttach(view: PreviewContract.View) {
        super.onAttach(view)
        addDisposable(imageClickDisposable())
    }

    private fun addDisposable(dispos: Disposable?) {
        if (dispos != null) {
            disposable.add(dispos)
        }
    }

    private fun imageClickDisposable(): Disposable? {
        if (mView == null) return null
        return mView!!.imageClickObservable()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView?.finishActivity()
                }
    }

}