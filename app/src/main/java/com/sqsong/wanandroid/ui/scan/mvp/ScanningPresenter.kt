package com.sqsong.wanandroid.ui.scan.mvp

import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ScanningPresenter @Inject constructor(private val disposable: CompositeDisposable) :
        BasePresenter<ScanningContract.View, IModel>(null, disposable) {

    override fun onAttach(view: ScanningContract.View) {
        super.onAttach(view)
    }

}