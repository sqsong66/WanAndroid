package com.sqsong.wanandroid.ui.home.mvp.knowledge

import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class ChildKnowledgeModel @Inject constructor(private val apiService: ApiService) : ChildKnowledgeContract.Model {
    override fun getKnowledgeChildList(page: Int, cid: Int): Observable<HomeItemBean> = apiService.getKnowledgeChildList(page, cid)

    override fun collectArticle(articleId: Int): Observable<BaseData> = apiService.collectArticle(articleId)

    override fun unCollectArticle(articleId: Int): Observable<BaseData> = apiService.unCollectArticle(articleId)

    override fun onDestroy() {
    }
}