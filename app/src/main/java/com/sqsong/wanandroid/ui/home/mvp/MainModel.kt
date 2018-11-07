package com.sqsong.wanandroid.ui.home.mvp

import com.sqsong.wanandroid.data.HotSearchBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class MainModel @Inject constructor(private val apiService: ApiService): MainContract.Model {
    override fun getHotKey(): Observable<HotSearchBean> = apiService.getHotKey()

    override fun onDestroy() {

    }
}