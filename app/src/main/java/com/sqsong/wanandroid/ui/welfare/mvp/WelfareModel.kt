package com.sqsong.wanandroid.ui.welfare.mvp

import com.sqsong.wanandroid.data.WelfareBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class WelfareModel @Inject constructor(private val apiService: ApiService) : WelfareContract.Model {

    override fun getWelfareList(page: Int): Observable<WelfareBean> = apiService.getWelfareList("福利", 20, page)

    override fun onDestroy() {
    }
}