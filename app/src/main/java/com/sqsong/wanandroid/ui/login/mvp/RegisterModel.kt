package com.sqsong.wanandroid.ui.login.mvp

import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable

class RegisterModel(private val apiService: ApiService) : RegisterContract.Model {

    override fun register(userName: String, password: String, rePassword: String): Observable<BaseData> = apiService.register(userName, password, rePassword)

    override fun onDestroy() {

    }
}