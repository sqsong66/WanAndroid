package com.sqsong.wanandroid.ui.home.mvp.navigation

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.NavigationBean
import com.sqsong.wanandroid.data.NavigationData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.adapter.NavigationAdapter
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.RxJavaHelper
import com.sqsong.wanandroid.util.recycler.FloatingTitleItemDecoration
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class NavigationPresenter @Inject constructor(private val navigationModel: NavigationModel,
                                              private val disposable: CompositeDisposable) :
        BasePresenter<NavigationContract.View, NavigationContract.Model>(navigationModel, disposable), OnItemClickListener<HomeItem> {

    private val mTitleMap = HashMap<Int, String>()
    private val mDataList = mutableListOf<NavigationData>()
    private val mAdapter by lazy {
        NavigationAdapter(mView.getFragmentContext(), mDataList)
    }

    override fun onAttach(view: NavigationContract.View) {
        super.onAttach(view)
        mView.showLoadingPage()
        initAdapter()
    }

    private fun initAdapter() {
        val padding = DensityUtil.dip2px(16).toFloat()
        val bgColor = CommonUtil.getThemeColor(mView.getFragmentContext(), R.attr.colorDefaultBackground)
        val textColor = CommonUtil.getThemeColor(mView.getFragmentContext(), R.attr.colorTextActive)
        val itemDecoration = FloatingTitleItemDecoration.Builder(mView.getFragmentContext())
                .setBackgroundColor(bgColor)
                .setTextColor(textColor)
                .setPadding(padding, padding, padding, padding)
                .setTextSize(16)
                .setRecyclerView(mView.getRecycler())
                .setTitleMap(mTitleMap)
                .build()
        mAdapter.setOnItemClickListener(this)
        mView.setRecyclerAdapter(mAdapter, itemDecoration)
    }

    fun requestNavigationList() {
        navigationModel.getNavigationList()
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<NavigationBean>(disposable) {
                    override fun onSuccess(bean: NavigationBean) {
                        if (bean.errorCode == 0) {
                            processNavigationData(bean.data)
                        } else {
                            mView.showMessage(bean.errorMsg)
                            mView.showErrorPage()
                        }
                    }

                    override fun onFail(error: ApiException) {
                        mView.showMessage(error.showMessage)
                        mView.showErrorPage()
                    }
                })
    }

    private fun processNavigationData(dataList: List<NavigationData>) {
        if (dataList.isEmpty()) {
            mView.showEmptyPage()
            return
        }
        mView.showContentPage()
        mTitleMap.clear()
        for (i in 0 until dataList.size) {
            mTitleMap[i] = dataList[i].name
        }
        mDataList.clear()
        mDataList.addAll(dataList)
        mAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(data: HomeItem?, position: Int) {

    }

}