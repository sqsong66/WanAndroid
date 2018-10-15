package com.sqsong.wanandroid.ui.home.mvp

import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.data.HomeBannerData
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

interface HomeContract {

    interface HomeView : IView {
        fun showHomeBanner(bannerData: MutableList<HomeBannerData>)
    }

    interface Model : IModel {
        fun getBannerData(): Observable<HomeBannerBean>
    }

}