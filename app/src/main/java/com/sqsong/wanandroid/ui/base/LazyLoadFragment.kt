package com.sqsong.wanandroid.ui.base

import android.os.Bundle
import com.sqsong.wanandroid.mvp.IPresenter

abstract class LazyLoadFragment<P : IPresenter<*>> : BaseFragment<P>() {

    private var mIsVisible = false
    private var mIsInitiated = false
    private var mIsPrepared = false

    /**
     * When the fragment first visible then load some data.
     */
    abstract fun loadInitData()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.mIsPrepared = true
        loadData(false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.mIsVisible = isVisibleToUser
        loadData(false)
    }

    private fun loadData(forceUpdate: Boolean) {
        if (mIsVisible && mIsPrepared && (!mIsInitiated || forceUpdate)) {
            loadInitData()
            mIsInitiated = true
        }
    }

}
