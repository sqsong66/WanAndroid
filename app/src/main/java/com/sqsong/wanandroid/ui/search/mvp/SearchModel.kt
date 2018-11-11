package com.sqsong.wanandroid.ui.search.mvp

import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class SearchModel @Inject constructor(private val apiService: ApiService) : SearchContract.Model {
    override fun query(page: Int, key: String): Observable<HomeItemBean> = apiService.query(page, key)
    override fun collectArticle(articleId: Int): Observable<BaseData> = apiService.collectArticle(articleId)
    override fun unCollectArticle(articleId: Int): Observable<BaseData> = apiService.unCollectArticle(articleId)
    override fun onDestroy() {
    }
}