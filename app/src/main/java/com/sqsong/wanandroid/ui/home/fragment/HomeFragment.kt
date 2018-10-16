package com.sqsong.wanandroid.ui.home.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.home.mvp.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.HomePresenter
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.fragment_home_backup.*
import javax.inject.Inject

class HomeFragment @Inject constructor() : BaseFragment<HomePresenter>(), HomeContract.HomeView, SwipeRefreshLayout.OnRefreshListener {

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
        recycler.layoutManager = LinearLayoutManager(context)
    }

    override fun onRefresh() {
        mPresenter.refreshData()
    }

    override fun setAdapter(adapter: HomeItemAdapter) {
        recycler.adapter = adapter
        adapter.setHomeItemActionListener(mPresenter)
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
}
