package com.sqsong.wanandroid.ui.wechat.mvp

import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class PublicAccountModel @Inject constructor(private val apiService: ApiService) : PublicAccountContract.Model {

    override fun getPublicAccountPeople(): Observable<KnowledgeBean> = apiService.getPublicAccountPeople()

    override fun getPublicAccountArticleList(id: Int, page: Int): Observable<HomeItemBean> = apiService.getPublicAccountArticleList(id, page)

    override fun collectArticle(articleId: Int): Observable<BaseData> = apiService.collectArticle(articleId)

    override fun unCollectArticle(articleId: Int): Observable<BaseData> = apiService.unCollectArticle(articleId)

    override fun onDestroy() {
    }
}