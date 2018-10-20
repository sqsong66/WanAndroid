package com.sqsong.wanandroid.ui.home.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.common.event.FabClickEvent
import com.sqsong.wanandroid.data.HomeBannerData
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.home.mvp.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.HomePresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.view.DefaultPageLayout
import com.sqsong.wanandroid.view.banner.BannerView
import kotlinx.android.synthetic.main.fragment_home_backup.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class HomeFragment @Inject constructor() : BaseFragment<HomePresenter>(), HomeContract.HomeView,
        SwipeRefreshLayout.OnRefreshListener, RecyclerScrollListener.OnLoadMoreListener {

    private var mBannerView: BannerView? = null
    private lateinit var mRecyclerScroller: RecyclerScrollListener

    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {
                    }
                }).build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_backup
    }

    override fun initEvent() {
        EventBus.getDefault().register(this)
        setupSwipeLayout()
        setupRecyclerView()
        mPresenter.onAttach(this)
    }

    @SuppressLint("ResourceType")
    private fun setupSwipeLayout() {
        val ta = context?.obtainStyledAttributes(TypedValue().data,
                intArrayOf(R.attr.colorPrimaryLight, R.attr.colorPrimary, R.attr.colorPrimaryDark))
        val lightColor = ta?.getColor(0, ContextCompat.getColor(context!!, R.color.colorPrimaryLight))
        val primaryColor = ta?.getColor(1, ContextCompat.getColor(context!!, R.color.colorPrimary))
        val primaryDarkColor = ta?.getColor(2, ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        swipeLayout.setColorSchemeColors(lightColor!!, primaryColor!!, primaryDarkColor!!)
        swipeLayout.setOnRefreshListener(this)
    }

    private fun setupRecyclerView() {
        recycler.recycledViewPool.setMaxRecycledViews(Constants.ITEM_TYPE_HEADER, 3)
        val layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager
        mRecyclerScroller = RecyclerScrollListener(layoutManager)
        recycler.addOnScrollListener(mRecyclerScroller)
        mRecyclerScroller.setOnLoadMoreListener(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFabClick(event: FabClickEvent) {
        if (event.index == 0) {
            recycler.smoothScrollToPosition(0)
        }
    }

    override fun onRefresh() {
        mPresenter.refreshData()
    }

    override fun onLoadMore() {
        mPresenter.loadMoreData()
    }

    override fun setAdapter(adapter: HomeItemAdapter) {
        recycler.adapter = adapter

        val headerView = LayoutInflater.from(context).inflate(R.layout.item_banner_header, null)
        mBannerView = headerView.findViewById(R.id.bannerView)
        adapter.setHeaderView(headerView)
        adapter.setHomeItemActionListener(mPresenter)
    }

    override fun showBannerData(bannerList: MutableList<HomeBannerData>) {
        mBannerView?.setBannerData(bannerList)
    }

    override fun showEmptyPage() {
        mPageLayout.showEmptyLayout()
    }

    override fun showLoadingPage() {
        mPageLayout.showLoadingLayout()
    }

    override fun showContentPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showContentLayout()
    }

    override fun getAppContext(): Context {
        return context!!
    }

    override fun loadFinish() {
        mRecyclerScroller.loadFinish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
