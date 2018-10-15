package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.data.HomeBannerBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("banner/json")
    fun getHomeBanner(): Observable<HomeBannerBean>

    @POST("lg/todo/listdone/0/json/1")
    fun getDoneTodoList(): Observable<HomeBannerBean>

}