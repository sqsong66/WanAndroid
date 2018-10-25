package com.sqsong.wanandroid.ui.home.mvp.knowledge

import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class ChildKnowledgeModel @Inject constructor(private val apiService: ApiService) : ChiledKnowledgeContract.Model {
    override fun getKnowledgeChildList(page: Int, cid: Int): Observable<HomeItemBean> = apiService.getKnowledgeChildList(page, cid)

    override fun onDestroy() {
    }
}