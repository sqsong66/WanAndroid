package com.sqsong.wanandroid.ui.home.mvp.knowledge

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeItemAdapter
import io.reactivex.Observable

interface ChildKnowledgeContract {

    interface View : IView {
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun getKnowledgeData(): KnowledgeData
        fun finishActivity()
        fun loadFinish()
        fun showLoginDialog()
        fun getActivityContext(): Context
        fun setRecyclerAdapter(adapter: KnowledgeItemAdapter)
        fun findRecyclerLastVisibleItemPosition(): Int
        fun getHandler(): Handler
        fun startNewActivity(intent: Intent)
    }

    interface Model : IModel {
        fun getKnowledgeChildList(page: Int, cid: Int): Observable<HomeItemBean>
        fun collectArticle(articleId: Int): Observable<BaseData>
        fun unCollectArticle(articleId: Int): Observable<BaseData>
    }

}