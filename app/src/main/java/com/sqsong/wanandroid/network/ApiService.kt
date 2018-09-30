package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.base.HomeBannerBean
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {

    @GET("banner/json")
    fun getHomeBanner(): Observable<HomeBannerBean>

}