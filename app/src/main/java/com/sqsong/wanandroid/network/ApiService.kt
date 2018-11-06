package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.data.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    // 首页banner
    @GET("banner/json")
    fun getHomeBanner(): Observable<HomeBannerBean>

    // 首页文章列表
    @GET("article/list/{page}/json")
    fun getHomeDataList(@Path("page") page: Int): Observable<HomeItemBean>

    // 注册
    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") userName: String, @Field("password") password: String,
                 @Field("repassword") rePassword: String): Observable<BaseData>

    // 登录
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") userName: String, @Field("password") password: String)
            : Observable<LoginBean>

    // 收藏文章
    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") articleId: Int): Observable<BaseData>

    // 收藏文章
    @POST("lg/uncollect_originId/{id}/json")
    fun unCollectArticle(@Path("id") articleId: Int): Observable<BaseData>

    // 体系列表
    @GET("tree/json")
    fun getKnowledgeList(): Observable<KnowledgeBean>

    // 体系文章列表
    @GET("article/list/{page}/json")
    fun getKnowledgeChildList(@Path("page") page: Int, @Query("cid") cid: Int): Observable<HomeItemBean>

    // 导航数据
    @GET("navi/json")
    fun getNavigationList(): Observable<NavigationBean>

    // 项目分类
    @GET("project/tree/json")
    fun getProjectClassify(): Observable<KnowledgeBean>

    // 项目列表
    @GET("project/list/{page}/json")
    fun getProjectList(@Path("page") page: Int, @Query("cid") cid: Int): Observable<HomeItemBean>

    // 获取公众号人物列表
    @GET("wxarticle/chapters/json")
    fun getPublicAccountPeople(): Observable<KnowledgeBean>

    // 获取公众号文章列表
    @GET("wxarticle/list/{id}/{page}/json")
    fun getPublicAccountArticleList(@Path("id") id: Int, @Path("page") page: Int): Observable<HomeItemBean>

    // 获取收藏列表
    @GET("lg/collect/list/{page}/json")
    fun getCollectionList(@Path("page") page: Int): Observable<HomeItemBean>
}