package com.sqsong.wanandroid.ui.home.mvp

import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class HomeModel @Inject constructor(private val apiService: ApiService) : HomeContract.Model {

    override fun getHomeDataList(page: Int): Observable<HomeItemBean> = apiService.getHomeDataList(page)

    override fun getBannerData(): Observable<HomeBannerBean> = apiService.getHomeBanner()

    override fun onDestroy() {
    }

}