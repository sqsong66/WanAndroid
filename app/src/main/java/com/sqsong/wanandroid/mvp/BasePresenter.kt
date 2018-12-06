package com.sqsong.wanandroid.mvp

open class BasePresenter<V : IView, M : IModel> constructor(private var mModel: M? = null) : IPresenter<V> {
    var mView: V? = null

    override fun onAttach(view: V) {
        checkNotNull(view) { "${view::class.java.simpleName} cannot be null." }
        mView = view
    }

    override fun onDestroy() {
        mView = null
        mModel?.onDestroy()
        mModel = null
    }

}