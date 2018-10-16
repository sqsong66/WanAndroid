package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.data.HomeItemBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("banner/json")
    fun getHomeBanner(): Observable<HomeBannerBean>

    @GET("article/list/{page}/json")
    fun getHomeDataList(@Path("page") page: Int): Observable<HomeItemBean>

    @POST("lg/todo/listdone/0/json/1")
    fun getDoneTodoList(): Observable<HomeBannerBean>

}