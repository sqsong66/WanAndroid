package com.sqsong.wanandroid.ui.home.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.TypedValue
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.NoLeakHandler
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeItemAdapter
import com.sqsong.wanandroid.ui.home.mvp.knowledge.ChildKnowledgeContract
import com.sqsong.wanandroid.ui.home.mvp.knowledge.ChildKnowledgePresenter
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.SnackbarUtil
import com.sqsong.wanandroid.util.setupToolbar
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.activity_knowledge.*
import kotlinx.android.synthetic.main.content_knowledge.*

@ChangeThemeAnnotation
class KnowledgeActivity : BaseActivity<ChildKnowledgePresenter>(), ChildKnowledgeContract.View,
        SwipeRefreshLayout.OnRefreshListener, RecyclerScrollListener.OnLoadMoreListener {

    private lateinit var mKnowledgeData: KnowledgeData
    private lateinit var mRecyclerScroller: RecyclerScrollListener

    private val mHandler: NoLeakHandler<KnowledgeActivity> by lazy {
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

    override fun beforeInflateView() {
        mKnowledgeData = intent.getParcelableExtra(Constants.KNOWLEDGE_DATA)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_knowledge
    }

    override fun initEvent() {
        setupToolbar(toolbar)
        mHandler.post { toolbar.title = mKnowledgeData.name }
        setupSwipeLayout()
        setupRecyclerView()
        mPresenter.onAttach(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ResourceType")
    private fun setupSwipeLayout() {
        val ta = this.obtainStyledAttributes(TypedValue().data,
                intArrayOf(R.attr.colorPrimaryLight, R.attr.colorPrimary, R.attr.colorPrimaryDark))
        val lightColor = ta?.getColor(0, ContextCompat.getColor(this, R.color.colorPrimaryLight))
        val primaryColor = ta?.getColor(1, ContextCompat.getColor(this, R.color.colorPrimary))
        val primaryDarkColor = ta?.getColor(2, ContextCompat.getColor(this, R.color.colorPrimaryDark))
        swipeLayout.setColorSchemeColors(lightColor!!, primaryColor!!, primaryDarkColor!!)
        swipeLayout.setOnRefreshListener(this)
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = mLayoutManager
        mRecyclerScroller = RecyclerScrollListener(mLayoutManager)
        recycler.addOnScrollListener(mRecyclerScroller)
        mRecyclerScroller.setOnLoadMoreListener(this)
    }

    override fun onRefresh() {
        showContentPage()
        mPresenter.refreshData()
    }

    override fun setRecyclerAdapter(adapter: KnowledgeItemAdapter) {
        recycler.adapter = adapter
    }

    override fun getHandler(): Handler = mHandler

    override fun onLoadMore() = mPresenter.loadMoreData()

    override fun getKnowledgeData(): KnowledgeData = mKnowledgeData

    override fun showLoadingPage() = mPageLayout.showLoadingLayout()

    override fun finishActivity() = finish()

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

    override fun loadFinish() {
        mRecyclerScroller.loadFinish()
    }

    override fun showMessage(message: String?) {
        SnackbarUtil.showToastText(this, message!!)
    }

    override fun getActivityContext(): Context {
        return this
    }

    override fun findRecyclerLastVisibleItemPosition(): Int = mLayoutManager.findLastVisibleItemPosition()

    override fun showLoginDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.text_login_tips_title)
                .setMessage(R.string.text_login_tips_message)
                .setCancelable(false)
                .setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.text_sure) { dialog, _ ->
                    run {
                        dialog.dismiss()
                        BaseApplication.INSTANCE.quitApp()
                        startActivity(Intent(this, LoginActivity::class.java))
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
