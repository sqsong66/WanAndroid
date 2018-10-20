package com.sqsong.wanandroid.ui.login.mvp

import com.sqsong.wanandroid.data.LoginBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class LoginModel @Inject constructor(private val apiService: ApiService) :
        LoginContract.Model {
    override fun login(userName: String, password: String): Observable<LoginBean> {
        return apiService.login(userName, password)
    }

    override fun onDestroy() {

    }
}