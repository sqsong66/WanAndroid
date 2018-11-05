package com.sqsong.wanandroid.ui.wechat.mvp

import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.web.WebViewActivity
import com.sqsong.wanandroid.ui.wechat.adapter.PublicAccountAdapter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable

class AccountPresenter(private val accountModel: PublicAccountModel,
                       private val disposable: CompositeDisposable) :
        BasePresenter<AccountContract.View, PublicAccountModel>(accountModel, disposable), HomeItemAdapter.HomeItemActionListener {


    private var mPage = 1
    private val mDataList = mutableListOf<HomeItem>()
    private val mPreferences: SharedPreferences by lazy {
        PreferenceHelper.defaultPrefs(mView.getFragmentContext())
    }

    private val mAdapter: PublicAccountAdapter by lazy {
        PublicAccountAdapter(mView.getFragmentContext(), mDataList)
    }

    override fun onAttach(view: AccountContract.View) {
        super.onAttach(view)
        mView.showLoadingPage()
        mView.setRecyclerAdapter(mAdapter)
        mAdapter.setHomeItemActionListener(this)
    }

    fun refreshData() {
        mPage = 0
        requestAccountList()
    }

    fun loadMore() {
        mPage++
        requestAccountList()
    }

    private fun requestAccountList() {
        accountModel.getPublicAccountArticleList(mView.getCid(), mPage)
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<HomeItemBean>(disposable) {
                    override fun onSuccess(bean: HomeItemBean) {
                        if (bean.errorCode == 0) {
                            setupDataList(bean.data.datas)
                        } else {
                            mView.showMessage(bean.errorMsg!!)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        mView.showMessage(error.showMessage)
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
            mDataList.clear()
        }
        mDataList.addAll(dataList)
        mAdapter.notifyDataSetChanged()

        mView.getHandler().post {
            if (mPage == 0 && mView.findRecyclerLastVisibleItemPosition() == mDataList.size) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_HIDDEN)
            } else if (dataList.size < Constants.PAGE_SIZE) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_LOADING)
                mView.loadFinish()
            }
        }
    }

    override fun onStarClick(homeItem: HomeItem, position: Int) {
        val userName: String = mPreferences[Constants.LOGIN_USER_NAME] ?: ""
        if (TextUtils.isEmpty(userName)) {
            mView.showLoginDialog()
            return
        }

        val collectState = homeItem.collect
        val requestObservable = if (collectState) accountModel.unCollectArticle(homeItem.id) else accountModel.collectArticle(homeItem.id)
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
        intent.putExtra(Constants.KEY_WEB_URL, homeItem.link)
        intent.putExtra(Constants.KEY_WEB_TITLE, homeItem.title)
        mView.startNewActivity(intent)
    }

    override fun onShareClick(homeItem: HomeItem, position: Int) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/html"
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, homeItem.link)
        sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE, homeItem.title)
        mView.startNewActivity(Intent.createChooser(sharingIntent, ""))
    }

}