package com.sqsong.wanandroid.ui.home.mvp

import android.content.Context
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
        fun setAdapter(adapter: HomeItemAdapter)
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
    }

    interface Model : IModel {
        fun getBannerData(): Observable<HomeBannerBean>

        fun getHomeDataList(page: Int): Observable<HomeItemBean>
    }

}