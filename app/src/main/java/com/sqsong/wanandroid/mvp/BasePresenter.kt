package com.sqsong.wanandroid.mvp

import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V : IView, M : IModel> constructor(private var mModel: M? = null,
                                                            private var mCompositeDisposable: CompositeDisposable? = null) : IPresenter<V> {
    lateinit var mView: V

    override fun onAttach(view: V) {
        checkNotNull(view) { "${view::class.java.simpleName} cannot be null." }
        mView = view
    }

    override fun onDestroy() {
        mModel?.onDestroy()
        mModel = null
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

}