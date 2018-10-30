package com.sqsong.wanandroid.ui.home.mvp.project

import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.data.*
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.home.adapter.ProjectAdapter
import com.sqsong.wanandroid.ui.web.WebViewActivity
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProjectPresenter @Inject constructor(private val projectModel: ProjectModel,
                                           private val disposable: CompositeDisposable) :
        BasePresenter<ProjectContract.View, ProjectContract.Model>(projectModel, disposable), HomeItemAdapter.HomeItemActionListener {

    @Inject
    lateinit var mPreferences: SharedPreferences

    private var mCid: Int = 0
    private var mPage: Int = 0
    private val mProjectList = mutableListOf<HomeItem>()
    private val mClassifyList = mutableListOf<KnowledgeData>()

    private val mAdapter by lazy {
        ProjectAdapter(mView.getFragmentContext(), mProjectList)
    }

    override fun onAttach(view: ProjectContract.View) {
        super.onAttach(view)
        mView.showLoadingPage()
        mAdapter.setHomeItemActionListener(this)
        mView.setRecyclerAdapter(mAdapter)
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
        if (mPage == 0) {
            mView.showErrorPage()
        } else {
            mPage--
            if (mPage < 0) mPage = 0
            mView.loadFinish()
        }
    }

    private fun processClassify(dataList: List<KnowledgeData>) {
        if (dataList.isEmpty()) {
            mView.showEmptyPage()
            return
        }
        mView.preparePopupWindow(dataList, true)

        mPage = 0
        mClassifyList.clear()
        mClassifyList.addAll(dataList)
        mCid = mClassifyList[0].id
        requestProjectList()
    }

    private fun requestProjectList() {
        projectModel.getProjectList(mPage, mCid)
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<HomeItemBean>(disposable) {
                    override fun onSuccess(bean: HomeItemBean) {
                        if (bean.errorCode == 0) {
                            setupDataList(bean.data.datas)
                        } else {
                            showError(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        showError(error.showMessage)
                    }
                })
    }

    private fun setupDataList(dataList: MutableList<HomeItem>) {
        mView.showContentPage()
        if (dataList.isEmpty()) {
            if (mPage == 0) {
                mView.showEmptyPage()
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            }
            return
        }
        if (mPage == 0) {
            mProjectList.clear()
        }
        mProjectList.addAll(dataList)
        mAdapter.notifyDataSetChanged()

        mView.getHandler().post {
            if (mPage == 0 && mView.findRecyclerLastVisibleItemPosition() == mProjectList.size) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_HIDDEN)
            } else if (dataList.size < 15/*Constants.PAGE_SIZE*/) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_LOADING)
                mView.loadFinish()
            }
        }
    }


    fun checkPopState() {
        if (mClassifyList.isEmpty()) return
        mView.showPopupWindow(mClassifyList)
    }

    fun loadMore() {
        mPage++
        requestProjectList()
    }

    fun refreshData(knowledgeData: KnowledgeData?) {
        if (knowledgeData == null || mCid == knowledgeData.id) return
        mPage = 0
        mCid = knowledgeData.id
        requestProjectList()
    }

    override fun onStarClick(homeItem: HomeItem, position: Int) {
        val userName: String = mPreferences[Constants.LOGIN_USER_NAME] ?: ""
        if (TextUtils.isEmpty(userName)) {
            mView.showLoginDialog()
            return
        }

        val collectState = homeItem.collect
        val requestObservable = if (collectState) projectModel.unCollectArticle(homeItem.id) else projectModel.collectArticle(homeItem.id)
        requestObservable.compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<BaseData>(disposable) {
                    override fun onSuccess(bean: BaseData) {
                        if (bean.errorCode == 0) {
                            if (collectState) {
                                homeItem.collect = false
                                mView.showMessage(mView.getFragmentContext().getString(R.string.text_cancel_collect_success))
                            } else {
                                homeItem.collect = true
                                mView.showMessage(mView.getFragmentContext().getString(R.string.text_collect_success))
                            }
                            mAdapter.notifyItemChanged(position)
                        } else {
                            mView.showMessage(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        mView.showMessage(error.showMessage)
                    }
                })
    }

    override fun onListItemClick(homeItem: HomeItem, position: Int) {
        val intent = Intent(mView.getFragmentContext(), WebViewActivity::class.java)
        intent.putExtra(Constants.KEY_WEB_URL, homeItem?.link)
        intent.putExtra(Constants.KEY_WEB_TITLE, homeItem?.title)
        mView.startNewActivity(intent)
    }
}