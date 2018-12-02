package com.sqsong.wanandroid.ui.collection

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.NoLeakHandler
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.collection.adapter.CollectionAdapter
import com.sqsong.wanandroid.ui.collection.mvp.CollectionContract
import com.sqsong.wanandroid.ui.collection.mvp.CollectionPresenter
import com.sqsong.wanandroid.util.SnackbarUtil
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.util.ext.setupToolbar
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.activity_knowledge.*
import kotlinx.android.synthetic.main.content_knowledge.*

/**
 * 收藏
 */
class CollectionActivity : BaseActivity<CollectionPresenter>(), CollectionContract.View, SwipeRefreshLayout.OnRefreshListener, RecyclerScrollListener.OnLoadMoreListener {

    private lateinit var mRecyclerScroller: RecyclerScrollListener

    private val mHandler: NoLeakHandler<CollectionActivity> by lazy {
        NoLeakHandler(this)
    }

    private val mLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
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

    override fun getLayoutResId(): Int = R.layout.activity_knowledge

    override fun initEvent() {
        setupToolbar(toolbar)
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener(this)
        setupRecyclerView()
        mHandler.post { toolbar.title = getString(R.string.text_collection) }
        mPresenter.onAttach(this)
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = mLayoutManager
        mRecyclerScroller = RecyclerScrollListener(mLayoutManager)
        recycler.addOnScrollListener(mRecyclerScroller)
        mRecyclerScroller.setOnLoadMoreListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        mPresenter.refreshData()
    }

    override fun onLoadMore() = mPresenter.loadMoreData()

    override fun showLoadingPage() = mPageLayout.showLoadingLayout()

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

    override fun loadFinish() = mRecyclerScroller.loadFinish()

    override fun getAppContext(): Context = this

    override fun setRecyclerAdapter(adapter: CollectionAdapter) {
        recycler.adapter = adapter
    }

    override fun getHandler(): Handler = mHandler

    override fun findRecyclerLastVisibleItemPosition(): Int = mLayoutManager.findLastVisibleItemPosition()

    override fun startNewActivity(intent: Intent) = startActivity(intent)

    override fun showMessage(message: String?) {
        SnackbarUtil.showToastText(this, message)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}