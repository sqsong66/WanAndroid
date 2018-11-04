package com.sqsong.wanandroid.ui.wechat.mvp

import androidx.fragment.app.FragmentManager
import com.sqsong.wanandroid.common.FragmentPagerAdapter
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

interface PublicAccountContract {

    interface View : IView {
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun fragmentManager(): FragmentManager
        fun setViewPagerAdapter(pagerAdapter: FragmentPagerAdapter)
    }

    interface Model : IModel {
        fun getPublicAccountPeople(): Observable<KnowledgeBean>
        fun getPublicAccountArticleList(id: Int, page: Int): Observable<HomeItemBean>
        fun collectArticle(articleId: Int): Observable<BaseData>
        fun unCollectArticle(articleId: Int): Observable<BaseData>
    }

}