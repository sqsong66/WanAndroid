package com.sqsong.wanandroid.ui.home.mvp

import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class HomeModel @Inject constructor(private val apiService: ApiService) : HomeContract.Model {

    override fun getBannerData(): Observable<HomeBannerBean> {
        return apiService.getHomeBanner()
    }

    override fun onDestroy() {

    }
}