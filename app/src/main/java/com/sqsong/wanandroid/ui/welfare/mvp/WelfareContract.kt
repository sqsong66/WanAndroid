package com.sqsong.wanandroid.ui.welfare.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.sqsong.wanandroid.data.WelfareBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.welfare.adapter.WelfareAdapter
import io.reactivex.Observable

interface WelfareContract {

    interface View : IView {
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun loadFinish()
        fun getAppContext(): Context
        fun getHandler(): Handler
        fun setRecyclerAdapter(adapter: WelfareAdapter)
        fun findRecyclerLastVisibleItemPosition(): Int
        fun startNewActivity(intent: Intent)
        fun startNewActivity(intent: Intent, bundle: Bundle)
    }

    interface Model : IModel {
        fun getWelfareList(page: Int): Observable<WelfareBean>
    }

}