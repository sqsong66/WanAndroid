package com.sqsong.wanandroid.ui.wechat.mvp

import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class PublicAccountModel @Inject constructor(private val apiService: ApiService) : PublicAccountContract.Model {
    override fun getPublicAccountPeople(): Observable<KnowledgeBean> = apiService.getPublicAccountPeople()

    override fun onDestroy() {
    }
}