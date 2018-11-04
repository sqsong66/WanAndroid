package com.sqsong.wanandroid.ui.home.mvp.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.event.FabClickEvent
import com.sqsong.wanandroid.common.event.SwitchIndexEvent
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.*
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.activity.KnowledgeActivity
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.web.WebViewActivity
import com.sqsong.wanandroid.ui.wechat.PublicAccountActivity
import com.sqsong.wanandroid.util.CommonUtil
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
        BasePresenter<HomeContract.HomeView, HomeContract.Model>(homeModel, disposable),
        HomeItemAdapter.HomeItemActionListener, OnItemClickListener<HomeBannerData> {

    private var mPage: Int = 0

    private val mAdapter: HomeItemAdapter by lazy {
        HomeItemAdapter(mView.getAppContext(), homeItemList)
    }

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
        val intent = Intent(mView.getAppContext(), WebViewActivity::class.java)
        intent.putExtra(Constants.KEY_WEB_URL, homeItem.link)
        intent.putExtra(Constants.KEY_WEB_TITLE, homeItem.title)
        mView.startNewActivity(intent)
    }

    override fun onShareClick(homeItem: HomeItem, position: Int) {
    }

    override fun onItemClick(data: HomeBannerData?, position: Int) {
        lateinit var intent: Intent
        if (data?.id == 4 && !TextUtils.isEmpty(CommonUtil.parseUrlParameter(data.url, "cid"))) { // 面试相关
            intent = Intent(mView.getAppContext(), KnowledgeActivity::class.java)
            intent.putExtra(Constants.KNOWLEDGE_CID, CommonUtil.parseUrlParameter(data.url, "cid")?.toInt())
            intent.putExtra(Constants.KNOWLEDGE_TITLE, mView.getAppContext().getString(R.string.text_interview_relative))
            mView.startNewActivity(intent)
        } else if (data?.id == 3) { // 完整项目
            EventBus.getDefault().post(SwitchIndexEvent(3))
        } else if (data?.id == 6) { // 专属导航
            EventBus.getDefault().post(SwitchIndexEvent(2))
        } else if (data?.id == 18) {
            intent = Intent(mView.getAppContext(), PublicAccountActivity::class.java)
            mView.startNewActivity(intent)
        } else {
            intent = Intent(mView.getAppContext(), WebViewActivity::class.java)
            intent.putExtra(Constants.KEY_WEB_URL, data?.url)
            intent.putExtra(Constants.KEY_WEB_TITLE, data?.title)
            mView.startNewActivity(intent)
        }
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