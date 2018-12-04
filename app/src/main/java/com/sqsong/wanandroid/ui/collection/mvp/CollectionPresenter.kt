package com.sqsong.wanandroid.ui.collection.mvp

import android.content.Intent
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.collection.adapter.CollectionAdapter
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.web.WebViewActivity
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CollectionPresenter @Inject constructor(private val collectionModel: CollectionModel,
                                              private val disposable: CompositeDisposable) :
        BasePresenter<CollectionContract.View, CollectionContract.Model>(collectionModel, disposable), HomeItemAdapter.HomeItemActionListener {

    private var mPage: Int = 0
    private val mDataList = mutableListOf<HomeItem>()
    private val mAdapter: CollectionAdapter by lazy {
        CollectionAdapter(mView?.getAppContext(), mDataList)
    }

    override fun onAttach(view: CollectionContract.View) {
        super.onAttach(view)
        init()
    }

    private fun init() {
        mView?.showLoadingPage()
        mView?.setRecyclerAdapter(mAdapter)
        mAdapter.setHomeItemActionListener(this)
        refreshData()
    }

    fun refreshData() {
        mPage = 0
        requestCollectionList()
    }

    fun loadMoreData() {
        mPage++
        requestCollectionList()
    }

    private fun requestCollectionList() {
        collectionModel.getCollectionList(mPage)
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<HomeItemBean>(disposable) {
                    override fun onSuccess(bean: HomeItemBean) {
                        if (bean.errorCode == 0) {
                            setupDataList(bean.data?.datas)
                        } else {
                            showErrors(bean.errorMsg!!)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        showErrors(error.showMessage)
                    }
                })
    }

    private fun showErrors(message: String) {
        mView?.showMessage(message)
        if (mPage == 0) {
            mView?.showErrorPage()
        } else {
            mPage--
            mView?.loadFinish()
        }
    }

    private fun setupDataList(dataList: List<HomeItem>?) {
        mView?.showContentPage()
        if (dataList == null || dataList.isEmpty()) {
            if (mPage == 0) {
                mView?.showEmptyPage()
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

        mView?.getHandler()?.post {
            if (mPage == 0 && mView?.findRecyclerLastVisibleItemPosition() == mDataList.size) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_HIDDEN)
            } else if (dataList.size < Constants.PAGE_SIZE) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_LOADING)
                mView?.loadFinish()
            }
        }
    }

    override fun onStarClick(homeItem: HomeItem, position: Int) {
        collectionModel.unCollectArticle(homeItem.originId)
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<BaseData>(disposable) {
                    override fun onSuccess(bean: BaseData) {
                        if (bean.errorCode == 0) {
                            mView?.showMessage(mView?.getAppContext()?.getString(R.string.text_cancel_collect_success))
                            mDataList.removeAt(position)
                            mAdapter.notifyItemRemoved(position)
                            if (mDataList.isEmpty()) {
                                mView?.showEmptyPage()
                            }
                            // 防止后续item错位
                            mView?.getHandler()?.postDelayed({ mAdapter.notifyDataSetChanged() }, 500)
                        } else {
                            mView?.showMessage(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        mView?.showMessage(error.showMessage)
                    }
                })
    }

    override fun onListItemClick(homeItem: HomeItem, position: Int) {
        val intent = Intent(mView?.getAppContext(), WebViewActivity::class.java)
        intent.putExtra(Constants.KEY_WEB_URL, homeItem.link)
        intent.putExtra(Constants.KEY_WEB_TITLE, homeItem.title)
        mView?.startNewActivity(intent)
    }

    override fun onShareClick(homeItem: HomeItem, position: Int) {
        val sharingIntent = CommonUtil.buildShareIntent(homeItem.title, homeItem.link)
        mView?.startNewActivity(Intent.createChooser(sharingIntent, mView?.getAppContext()?.getString(R.string.text_share_link)))
    }

}