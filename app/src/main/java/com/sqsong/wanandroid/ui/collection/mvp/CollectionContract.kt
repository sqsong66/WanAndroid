package com.sqsong.wanandroid.ui.collection.mvp

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.collection.adapter.CollectionAdapter
import io.reactivex.Observable

interface CollectionContract {

    interface View : IView {
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun getAppContext(): Context
        fun setRecyclerAdapter(adapter: CollectionAdapter)
        fun getHandler(): Handler
        fun findRecyclerLastVisibleItemPosition(): Int
        fun loadFinish()
        fun startNewActivity(intent: Intent)
    }

    interface Model : IModel {
        fun collectArticle(articleId: Int): Observable<BaseData>
        fun unCollectArticle(articleId: Int): Observable<BaseData>
        fun getCollectionList(page: Int): Observable<HomeItemBean>
    }

}