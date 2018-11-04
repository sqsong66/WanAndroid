package com.sqsong.wanandroid.ui.wechat.mvp

import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PublicAccountPresenter @Inject constructor(private val accountModel: PublicAccountContract.Model,
                                                 private val disposable: CompositeDisposable) :
        BasePresenter<PublicAccountContract.View, PublicAccountContract.Model>(accountModel, disposable) {

    private val mDataList = mutableListOf<KnowledgeData>()

    override fun onAttach(view: PublicAccountContract.View) {
        super.onAttach(view)
        mView.showLoadingPage()
        requestAccountPeople()
    }

    private fun requestAccountPeople() {
        accountModel.getPublicAccountPeople()
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<KnowledgeBean>(disposable) {
                    override fun onSuccess(bean: KnowledgeBean) {
                        if (bean.errorCode == 0) {
                            prepareTitleAndFragments(bean.data)
                        } else {
                            showErrorPage(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        showErrorPage(error.showMessage)
                    }
                })
    }

    private fun prepareTitleAndFragments(dataList: List<KnowledgeData>) {
        if (dataList.isEmpty()) mView.showErrorPage()
        val titleList = mutableListOf<String>()
        for (data in dataList) {
            titleList.add(data.name)
        }

    }

    private fun showErrorPage(message: String?) {
        mView.showMessage(message)
        mView.showErrorPage()
    }
}