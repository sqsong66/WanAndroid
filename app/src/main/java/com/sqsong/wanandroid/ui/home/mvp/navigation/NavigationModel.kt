package com.sqsong.wanandroid.ui.home.mvp.navigation

import com.sqsong.wanandroid.data.NavigationBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class NavigationModel @Inject constructor(private val apiService: ApiService) : NavigationContract.Model {

    override fun getNavigationList(): Observable<NavigationBean> = apiService.getNavigationList()

    override fun onDestroy() {
    }

}