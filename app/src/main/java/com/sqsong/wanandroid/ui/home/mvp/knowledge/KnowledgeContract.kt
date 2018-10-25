package com.sqsong.wanandroid.ui.home.mvp.knowledge

import android.content.Context
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeAdapter
import io.reactivex.Observable

interface KnowledgeContract {

    interface View : IView {
        fun getFragmentContext(): Context
        fun setRecyclerAdapter(adapter: KnowledgeAdapter)
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun scrollRecycler(position: Int)
        fun startKnowledgeActivity(data: KnowledgeData?)
    }

    interface Model : IModel {
        fun getKnowledgeList(): Observable<KnowledgeBean>
    }

}