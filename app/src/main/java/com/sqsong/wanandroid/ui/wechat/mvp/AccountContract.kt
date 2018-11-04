package com.sqsong.wanandroid.ui.wechat.mvp

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.wechat.adapter.PublicAccountAdapter

/**
 * 公众号Fragment Contract
 */
interface AccountContract {

    interface View : IView {
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun setPresenter(presenter: AccountPresenter)
        fun getCid(): Int
        fun getFragmentContext(): Context
        fun setRecyclerAdapter(adapter: PublicAccountAdapter)
        fun getHandler(): Handler
        fun findRecyclerLastVisibleItemPosition(): Int
        fun loadFinish()
        fun showLoginDialog()
        fun startNewActivity(intent: Intent)
    }

}