package com.sqsong.wanandroid.ui.wechat.mvp

import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AccountPresenter @Inject constructor(private val accountModel: AccountContract.Model,
                                           private val disposable: CompositeDisposable) :
        BasePresenter<AccountContract.View, AccountContract.Model>(accountModel, disposable) {

    private var mPage = 0
    private val mDataList = mutableListOf<HomeItem>()

    override fun onAttach(view: AccountContract.View) {
        super.onAttach(view)
        mView.showLoadingPage()
        requestAccountList()
    }

    private fun requestAccountList() {
        accountModel.getPublicAccountArticleList(0, mPage)
                .compose(RxJavaHelper.compose())
                .subscribe(object: ObserverImpl<HomeItemBean>(disposable) {
                    override fun onSuccess(bean: HomeItemBean) {

                    }

                    override fun onFail(error: ApiException) {

                    }
                })
    }

}