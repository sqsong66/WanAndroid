package com.sqsong.wanandroid.ui.welfare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.MediaGridInset
import com.sqsong.wanandroid.common.NoLeakHandler
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.welfare.adapter.WelfareAdapter
import com.sqsong.wanandroid.ui.welfare.mvp.WelfareContract
import com.sqsong.wanandroid.ui.welfare.mvp.WelfarePresenter
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.util.ext.setupToolbar
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.activity_welfare.*
import kotlinx.android.synthetic.main.content_knowledge.*

@ChangeThemeAnnotation
class WelfareActivity : BaseActivity<WelfarePresenter>(), WelfareContract.View, SwipeRefreshLayout.OnRefreshListener,
        RecyclerScrollListener.OnLoadMoreListener {

    private lateinit var mRecyclerScroller: RecyclerScrollListener

    private val mHandler: NoLeakHandler<WelfareActivity> by lazy {
        NoLeakHandler(this)
    }

    private val mLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(this, 2)
    }

    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(this)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {
                        onRefresh()
                    }
                }).build()
    }

    override fun getLayoutResId(): Int = R.layout.activity_welfare

    override fun initEvent() {
        setupToolbar(toolbar)
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener(this)
        setupRecyclerView()
        mPresenter.onAttach(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = mLayoutManager
        mRecyclerScroller = RecyclerScrollListener(mLayoutManager)
        recycler.addOnScrollListener(mRecyclerScroller)
        mRecyclerScroller.setOnLoadMoreListener(this)

        val spacing = resources.getDimensionPixelSize(R.dimen.picture_grid_spacing)
        recycler.addItemDecoration(MediaGridInset(2, spacing, true))
    }

    override fun onRefresh() = mPresenter.refreshWelfare()

    override fun onLoadMore() = mPresenter.loadMoreWelfare()

    override fun getHandler(): Handler = mHandler

    override fun showLoadingPage() = mPageLayout.showLoadingLayout()

    override fun loadFinish() = mRecyclerScroller.loadFinish()

    override fun getAppContext(): Context = this

    override fun findRecyclerLastVisibleItemPosition(): Int = mLayoutManager.findLastVisibleItemPosition()

    override fun showContentPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showContentLayout()
    }

    override fun showErrorPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showErrorLayout()
    }

    override fun showEmptyPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showEmptyLayout()
    }

    override fun setRecyclerAdapter(adapter: WelfareAdapter) {
        recycler.adapter = adapter
    }

    override fun startNewActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun startNewActivity(intent: Intent, bundle: Bundle) {
        startActivity(intent, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}