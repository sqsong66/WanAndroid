package com.sqsong.wanandroid.ui.search.mvp

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.search.adapter.SearchAdapter
import io.reactivex.Observable

interface SearchContract {

    interface View : IView {
        fun getInitKey(): String?
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun searchObservable(): Observable<CharSequence>
        fun getAppContext(): Context
        fun setRecyclerAdapter(adapter: SearchAdapter)
        fun getHandler(): Handler
        fun findRecyclerLastVisibleItemPosition(): Int
        fun loadFinish()
        fun showLoginDialog()
        fun startNewActivity(intent: Intent)
        fun showClear(show: Boolean)
    }

    interface Model : IModel {
        fun query(page: Int, key: String): Observable<HomeItemBean>
        fun collectArticle(articleId: Int): Observable<BaseData>
        fun unCollectArticle(articleId: Int): Observable<BaseData>
    }

}