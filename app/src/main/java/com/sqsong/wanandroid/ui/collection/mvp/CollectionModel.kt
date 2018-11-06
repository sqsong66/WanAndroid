package com.sqsong.wanandroid.ui.collection.mvp

import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class CollectionModel @Inject constructor(private val apiService: ApiService) : CollectionContract.Model {

    override fun collectArticle(articleId: Int): Observable<BaseData> = apiService.collectArticle(articleId)

    override fun unCollectArticle(articleId: Int): Observable<BaseData> = apiService.unCollectArticle(articleId)

    override fun getCollectionList(page: Int): Observable<HomeItemBean> = apiService.getCollectionList(page)

    override fun onDestroy() {
    }
}