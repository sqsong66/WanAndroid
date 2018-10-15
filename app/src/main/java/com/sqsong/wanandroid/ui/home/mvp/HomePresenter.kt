package com.sqsong.wanandroid.ui.home.mvp

import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomePresenter @Inject constructor(private val homeView: HomeContract.HomeView,
                                        private val homeModel: HomeContract.Model,
                                        private val disposable: CompositeDisposable) :
        BasePresenter<HomeContract.HomeView, HomeContract.Model>() {

    override fun onCreate() {
        requestBannerData()
    }

    private fun requestBannerData() {
        homeModel.getBannerData()
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<HomeBannerBean>(disposable) {
                    override fun onSuccess(bean: HomeBannerBean) {
                        if (bean.errorCode == 0) {
                            homeView.showHomeBanner(bean.data)
                        } else{
                            homeView.showMessage(bean.errorMsg!!)
                        }
                    }

                    override fun onFail(error: ApiException) {

                    }
                })
    }

}