package com.sqsong.wanandroid.ui.home.mvp.knowledge

import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class KnowledgeModel @Inject constructor(private val apiService: ApiService) : KnowledgeContract.Model {
    override fun getKnowledgeList(): Observable<KnowledgeBean> = apiService.getKnowledgeList()

    override fun onDestroy() {
    }
}