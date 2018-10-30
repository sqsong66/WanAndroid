package com.sqsong.wanandroid.ui.home.mvp.project

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.home.adapter.ProjectAdapter
import io.reactivex.Observable

interface ProjectContract {

    interface View : IView {
        fun getFragmentContext(): Context
        fun setRecyclerAdapter(adapter: ProjectAdapter)
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun showLoginDialog()
        fun scrollRecycler(position: Int)
        fun preparePopupWindow(dataList: List<KnowledgeData>, forceReCreate: Boolean)
        fun showPopupWindow(classifyList: MutableList<KnowledgeData>)
        fun findRecyclerLastVisibleItemPosition(): Int
        fun getHandler(): Handler
        fun loadFinish()
        fun startNewActivity(intent: Intent)
    }

    interface Model : IModel {
        fun getProjectClassify(): Observable<KnowledgeBean>
        fun getProjectList(page: Int, cid: Int): Observable<HomeItemBean>
        fun collectArticle(articleId: Int): Observable<BaseData>
        fun unCollectArticle(articleId: Int): Observable<BaseData>
    }

}