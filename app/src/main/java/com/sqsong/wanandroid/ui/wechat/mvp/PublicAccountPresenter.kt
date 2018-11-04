package com.sqsong.wanandroid.ui.wechat.mvp

import androidx.fragment.app.Fragment
import com.sqsong.wanandroid.common.FragmentPagerAdapter
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.wechat.fragment.PublicAccountFragment
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PublicAccountPresenter @Inject constructor(private val accountModel: PublicAccountModel,
                                                 private val disposable: CompositeDisposable) :
        BasePresenter<PublicAccountContract.View, PublicAccountContract.Model>(accountModel, disposable) {

    override fun onAttach(view: PublicAccountContract.View) {
        super.onAttach(view)
        mView.showLoadingPage()
        requestAccountPeople()
    }

    fun requestAccountPeople() {
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
        mView.showContentPage()
        if (dataList.isEmpty()) mView.showErrorPage()
        val titleList = mutableListOf<String>()
        val fragmentList = mutableListOf<Fragment>()
        for (data in dataList) {
            titleList.add(data.name)
            val fragment = PublicAccountFragment.newInstance(data.id)
            fragment.setPresenter(AccountPresenter(accountModel, disposable))
            fragmentList.add(fragment)
        }
        val pagerAdapter = FragmentPagerAdapter(mView.fragmentManager(), fragmentList, titleList)
        mView.setViewPagerAdapter(pagerAdapter)
    }

    private fun showErrorPage(message: String?) {
        mView.showMessage(message)
        mView.showErrorPage()
    }
}