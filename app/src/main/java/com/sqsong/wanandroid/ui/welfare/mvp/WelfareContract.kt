package com.sqsong.wanandroid.ui.welfare.mvp

import android.content.Context
import android.os.Handler
import com.sqsong.wanandroid.data.WelfareBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.welfare.WelfareAdapter
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
    }

    interface Model : IModel {
        fun getWelfareList(page: Int): Observable<WelfareBean>
    }

}