package com.sqsong.wanandroid.ui.home.mvp.home

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.event.FabClickEvent
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeBannerBean
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class HomePresenter @Inject constructor(private val homeModel: HomeModel,
        /*private val homeView: HomeContract.HomeView,*/
                                        private val disposable: CompositeDisposable) :
        BasePresenter<HomeContract.HomeView, HomeContract.Model>(homeModel, disposable), HomeItemAdapter.HomeItemActionListener {

    private var mPage: Int = 0

    private lateinit var mAdapter: HomeItemAdapter

    private var homeItemList = mutableListOf<HomeItem>()

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mPreferences: SharedPreferences

    override fun onAttach(view: HomeContract.HomeView) {
        super.onAttach(view)
        setupAdapter()
        refreshData()
    }

    private fun setupAdapter() {
        EventBus.getDefault().register(this)

        mAdapter = HomeItemAdapter(mView.getAppContext(), homeItemList)
        mAdapter.setHeaderView(mView.getBannerHeaderView())
        mAdapter.setHomeItemActionListener(this)
        mView.setAdapter(mAdapter)
        mView.showLoadingPage()
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
                            mView.showBannerData(bean.data)
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
        mView.showMessage(message)
        if (mPage == 0) {
            mView.showErrorPage()
        } else {
            mPage--
            mView.loadFinish()
        }
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
            mAdapter.resetAnimationPosition()
        }
        homeItemList.addAll(dataList)
        mAdapter.notifyDataSetChanged()
        mView.loadFinish()
    }

    override fun onStarClick(homeItem: HomeItem, position: Int) {
        val userName: String = mPreferences[Constants.LOGIN_USER_NAME] ?: ""
        if (TextUtils.isEmpty(userName)) {
            mView.showLoginDialog()
            return
        }

        val collectState = homeItem.collect
        val requestObservable = if (collectState) homeModel.unCollectArticle(homeItem.id) else homeModel.collectArticle(homeItem.id)
        requestObservable.compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<BaseData>(disposable) {
                    override fun onSuccess(bean: BaseData) {
                        if (bean.errorCode == 0) {
                            if (collectState) {
                                homeItem.collect = false
                                mView.showMessage(mContext.getString(R.string.text_cancel_collect_success))
                            } else {
                                homeItem.collect = true
                                mView.showMessage(mContext.getString(R.string.text_collect_success))
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFabClick(event: FabClickEvent) {
        if (event.index == 0) {
            mView.scrollRecycler(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}