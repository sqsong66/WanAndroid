package com.sqsong.wanandroid.ui.home.mvp.project

import android.content.Context
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeAdapter
import io.reactivex.Observable

interface ProjectContract {

    interface View : IView {
        fun getFragmentContext(): Context
        fun setRecyclerAdapter(adapter: KnowledgeAdapter)
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun scrollRecycler(position: Int)
        fun preparePopupWindow(dataList: List<KnowledgeData>, forceReCreate: Boolean)
    }

    interface Model : IModel {
        fun getProjectClassify(): Observable<KnowledgeBean>
        fun getProjectList(page: Int, cid: Int): Observable<HomeItemBean>
    }

}