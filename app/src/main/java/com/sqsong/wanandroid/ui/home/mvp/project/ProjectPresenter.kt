package com.sqsong.wanandroid.ui.home.mvp.project

import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProjectPresenter @Inject constructor(private val projectModel: ProjectModel,
                                           private val disposable: CompositeDisposable) :
        BasePresenter<ProjectContract.View, ProjectContract.Model>(projectModel, disposable) {

    private var mPage: Int = 0

    override fun onAttach(view: ProjectContract.View) {
        super.onAttach(view)

    }

    fun loadInitData() {
        projectModel.getProjectClassify()
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<KnowledgeBean>(disposable) {
                    override fun onSuccess(bean: KnowledgeBean) {
                        if (bean.errorCode == 0) {
                            processClassify(bean.data)
                        } else {
                            showError(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        showError(error.showMessage)
                    }
                })
    }

    private fun showError(errorMsg: String?) {
        mView.showMessage(errorMsg)
        mView.showErrorPage()
    }

    private fun processClassify(dataList: List<KnowledgeData>) {
        if (dataList.isEmpty()) {
            mView.showEmptyPage()
            return
        }
        mView.preparePopupWindow(dataList, true)

        mPage = 0
        requestProject(dataList[0].id)
    }

    fun requestProject(cid: Int) {

    }

}