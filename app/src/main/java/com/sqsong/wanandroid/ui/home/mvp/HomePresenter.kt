package com.sqsong.wanandroid.ui.home.mvp

import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomePresenter @Inject constructor(private val homeModel: HomeModel,
        /*@Inject @FragmentView("homeView") var homeView: HomeContract.HomeView,*/
                                        private val disposable: CompositeDisposable) :
        BasePresenter<HomeContract.HomeView, HomeContract.Model>(homeModel, disposable), HomeItemAdapter.HomeItemActionListener {

    private var homeItemList = mutableListOf<HomeItem>()

    lateinit var mAdapter: HomeItemAdapter

    private var mPage: Int = 0

    override fun onAttach(view: HomeContract.HomeView) {
        super.onAttach(view)
        mAdapter = HomeItemAdapter(mView.getAppContext(), homeItemList)
        mView.setAdapter(mAdapter)
        mView.showLoadingPage()
        refreshData()
    }

    fun refreshData() {
        mPage = 0
        requestBannerData()
        requestHomeList()
    }

    fun loadMoreData() {
        mPage++
        requestHomeList()
    }

    private fun requestBannerData() {
        homeModel.getBannerData()
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<HomeBannerBean>(disposable) {
                    override fun onSuccess(bean: HomeBannerBean) {
                        if (bean.errorCode == 0) {
                            mView.showContentPage()
                            // mAdapter.setBannerList(bean.data)
                            mView.showBannerData(bean.data)
                        } else {
                            mView.showMessage(bean.errorMsg!!)
                        }
                    }

                    override fun onFail(error: ApiException) {

                    }
                })
    }

    private fun requestHomeList() {
        homeModel.getHomeDataList(mPage)
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
            homeItemList.clear()
        }
        homeItemList.addAll(dataList)
        mAdapter.notifyDataSetChanged()
        mView.loadFinish()
    }

    override fun onStarClick(homeItem: HomeItem, position: Int) {

    }

    override fun onListItemClick(homeItem: HomeItem, position: Int) {

    }

}