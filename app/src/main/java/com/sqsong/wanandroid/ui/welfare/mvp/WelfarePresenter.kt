package com.sqsong.wanandroid.ui.welfare.mvp

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.util.Pair
import android.view.View
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.common.inter.OnViewItemClickListener
import com.sqsong.wanandroid.data.WelfareBean
import com.sqsong.wanandroid.data.WelfareData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.GankObserverImpl
import com.sqsong.wanandroid.ui.preview.ImagePreviewActivity
import com.sqsong.wanandroid.ui.welfare.adapter.WelfareAdapter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WelfarePresenter @Inject constructor(private val welfareModel: WelfareModel,
                                           private val disposable: CompositeDisposable) :
        BasePresenter<WelfareContract.View, WelfareContract.Model>(welfareModel, disposable), OnViewItemClickListener<WelfareData> {

    private var mPage = 1
    private val mDataList = ArrayList<WelfareData>()

    private val mAdapter: WelfareAdapter by lazy {
        WelfareAdapter(mView.getAppContext(), mDataList)
    }

    override fun onAttach(view: WelfareContract.View) {
        super.onAttach(view)
        init()
    }

    private fun init() {
        mView.showLoadingPage()
        mAdapter.setOnItemClickListener(this)
        mView.setRecyclerAdapter(mAdapter)
        refreshWelfare()
    }

    fun refreshWelfare() {
        mPage = 1
        requestWelfareList()
    }

    fun loadMoreWelfare() {
        mPage++
        requestWelfareList()
    }

    private fun requestWelfareList() {
        welfareModel.getWelfareList(mPage)
                .compose(RxJavaHelper.compose())
                .subscribe(object : GankObserverImpl<WelfareBean>(disposable) {
                    override fun onSuccess(bean: WelfareBean) {
                        if (!bean.error) {
                            setupDataList(bean.results)
                        } else {
                            showErrors(mView.getAppContext().getString(R.string.text_error_tips))
                        }
                    }

                    override fun onFail(error: ApiException) {
                        showErrors(error.showMessage)
                    }
                })
    }

    private fun showErrors(message: String) {
        mView.showMessage(message)
        if (mPage == 1) {
            mView.showErrorPage()
        } else {
            mPage--
            mView.loadFinish()
        }
    }

    private fun setupDataList(dataList: List<WelfareData>?) {
        mView.showContentPage()
        if (dataList == null || dataList.isEmpty()) {
            if (mPage == 1) {
                mView.showEmptyPage()
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            }
            return
        }
        if (mPage == 1) {
            mDataList.clear()
        }
        mDataList.addAll(dataList)
        mAdapter.notifyDataSetChanged()

        mView.getHandler().post {
            if (mPage == 1 && mView.findRecyclerLastVisibleItemPosition() == mDataList.size) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_HIDDEN)
            } else if (dataList.size < Constants.PAGE_SIZE) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_LOADING)
                mView.loadFinish()
            }
        }
    }

    override fun onItemClick(view: View, data: WelfareData?, position: Int) {
        val intent = Intent(mView.getAppContext(), ImagePreviewActivity::class.java)
        intent.putExtra(Constants.IMAGE_POSITION, position)
        intent.putParcelableArrayListExtra(Constants.IMAGE_LIST, mDataList)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val activityOptions = ActivityOptions.makeSceneTransitionAnimation(mView.getAppContext() as Activity,
                    Pair.create(view, view.transitionName))
            mView.startNewActivity(intent, activityOptions.toBundle())
        } else {
            mView.startNewActivity(intent)
        }
    }

}