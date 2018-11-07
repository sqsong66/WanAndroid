package com.sqsong.wanandroid.ui.home.mvp.home

import android.content.Context
import android.content.Intent
import android.view.View
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.data.HomeBannerData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import io.reactivex.Observable

interface HomeContract {

    interface HomeView : IView {
        fun getAppContext(): Context
        fun getBannerHeaderView(): View
        fun setAdapter(adapter: HomeItemAdapter)
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun showBannerData(bannerList: List<HomeBannerData>?)
        fun scrollRecycler(position: Int)
        fun loadFinish()
        fun showLoginDialog()
        fun startNewActivity(intent: Intent)
    }

    interface Model : IModel {
        fun getBannerData(): Observable<HomeBannerBean>

        fun getHomeDataList(page: Int): Observable<HomeItemBean>

        fun collectArticle(articleId: Int): Observable<BaseData>

        fun unCollectArticle(articleId: Int): Observable<BaseData>
    }

}