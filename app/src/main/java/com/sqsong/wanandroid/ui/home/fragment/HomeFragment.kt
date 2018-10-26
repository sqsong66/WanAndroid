package com.sqsong.wanandroid.ui.home.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.data.HomeBannerData
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.home.mvp.home.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.home.HomePresenter
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.view.DefaultPageLayout
import com.sqsong.wanandroid.view.banner.BannerView
import kotlinx.android.synthetic.main.fragment_home_backup.*
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
                        onRefresh()
                    }
                }).build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_backup
    }

    override fun initEvent() {
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
        val layoutManager = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager
        mRecyclerScroller = RecyclerScrollListener(layoutManager)
        recycler.addOnScrollListener(mRecyclerScroller)
        mRecyclerScroller.setOnLoadMoreListener(this)
    }

    override fun onRefresh() {
        showContentPage()
        mPresenter.refreshData()
    }

    override fun onLoadMore() {
        mPresenter.loadMoreData()
    }

    override fun getBannerHeaderView(): View {
        val headerView = LayoutInflater.from(context).inflate(R.layout.item_banner_header, null)
        mBannerView = headerView.findViewById(R.id.bannerView)
        return headerView
    }

    override fun setAdapter(adapter: HomeItemAdapter) {
        recycler.adapter = adapter
    }

    override fun showBannerData(bannerList: MutableList<HomeBannerData>) {
        mBannerView?.setBannerData(bannerList)
    }

    override fun showEmptyPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showEmptyLayout()
    }

    override fun showLoadingPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showLoadingLayout()
    }

    override fun showContentPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showContentLayout()
    }

    override fun showErrorPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showErrorLayout()
    }

    override fun getAppContext(): Context {
        return context!!
    }

    override fun loadFinish() {
        mRecyclerScroller.loadFinish()
    }

    override fun scrollRecycler(position: Int) {
        recycler.smoothScrollToPosition(position)
    }

    override fun showLoginDialog() {
        AlertDialog.Builder(activity!!)
                .setTitle(R.string.text_login_tips_title)
                .setMessage(R.string.text_login_tips_message)
                .setCancelable(false)
                .setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.text_sure) { dialog, _ ->
                    run {
                        dialog.dismiss()
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    }
                }
                .create()
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}
