package com.sqsong.wanandroid.ui.wechat.mvp

import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class AccountModel @Inject constructor(private val apiService: ApiService) : AccountContract.Model {
    override fun getPublicAccountArticleList(id: Int, page: Int): Observable<HomeItemBean> = apiService.getPublicAccountArticleList(id, page)

    override fun onDestroy() {
    }
}