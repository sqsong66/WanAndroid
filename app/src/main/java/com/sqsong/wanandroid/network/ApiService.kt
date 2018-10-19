package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.data.BaseBean
import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.data.HomeItemBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // 首页banner
    @GET("banner/json")
    fun getHomeBanner(): Observable<HomeBannerBean>

    // 首页文章列表
    @GET("article/list/{page}/json")
    fun getHomeDataList(@Path("page") page: Int): Observable<HomeItemBean>

    // 注册
    @POST("user/register")
    fun register(@Field("username") userName: String, @Field("password") password: String,
                 @Field("repassword") rePassword: String): Observable<BaseBean<*>>

}