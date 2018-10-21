package com.sqsong.wanandroid.ui.home.mvp.home

import android.content.Context
import android.view.View
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
        fun showBannerData(bannerList: MutableList<HomeBannerData>)
        fun loadFinish()
    }

    interface Model : IModel {
        fun getBannerData(): Observable<HomeBannerBean>

        fun getHomeDataList(page: Int): Observable<HomeItemBean>
    }

}