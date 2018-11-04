package com.sqsong.wanandroid.ui.wechat.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.NoLeakHandler
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.ui.base.LazyLoadFragment
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.ui.wechat.adapter.PublicAccountAdapter
import com.sqsong.wanandroid.ui.wechat.mvp.AccountContract
import com.sqsong.wanandroid.ui.wechat.mvp.AccountPresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.fragment_home_backup.*

class PublicAccountFragment : LazyLoadFragment(), AccountContract.View,
        RecyclerScrollListener.OnLoadMoreListener {


    private var mCid: Int = 0
    private var mPresenter: AccountPresenter? = null
    private lateinit var mRecyclerScroller: RecyclerScrollListener

    private val mLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    private val mHandler: NoLeakHandler<PublicAccountFragment> by lazy {
        NoLeakHandler(this)
    }

    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {
                        loadInitData()
                    }
                }).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCid = arguments?.getInt(Constants.KNOWLEDGE_CID, 0) ?: 0
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_backup
    }

    override fun initEvent() {
        setHasOptionsMenu(true)
        setupRecyclerView()
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener { mPresenter?.refreshData() }
        mPresenter?.onAttach(this)
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = mLayoutManager
        mRecyclerScroller = RecyclerScrollListener(mLayoutManager)
        recycler.addOnScrollListener(mRecyclerScroller)
        mRecyclerScroller.setOnLoadMoreListener(this)
    }

    override fun loadInitData() {
        mPresenter?.refreshData()
    }

    override fun onLoadMore() {
        mPresenter?.loadMore()
    }

    override fun setPresenter(presenter: AccountPresenter) {
        this.mPresenter = presenter
    }

    override fun getHandler(): Handler = mHandler

    override fun findRecyclerLastVisibleItemPosition(): Int = mLayoutManager.findLastVisibleItemPosition()

    override fun loadFinish() = mRecyclerScroller.loadFinish()

    override fun getFragmentContext(): Context = context!!

    override fun setRecyclerAdapter(adapter: PublicAccountAdapter) {
        recycler.adapter = adapter
    }

    override fun getCid(): Int = mCid

    override fun showLoadingPage() = mPageLayout.showLoadingLayout()

    override fun showEmptyPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showEmptyLayout()
    }

    override fun showContentPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showContentLayout()
    }

    override fun showErrorPage() {
        swipeLayout.isRefreshing = false
        mPageLayout.showErrorLayout()
    }

    override fun startNewActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun showLoginDialog() {
        AlertDialog.Builder(context!!)
                .setTitle(R.string.text_login_tips_title)
                .setMessage(R.string.text_login_tips_message)
                .setCancelable(false)
                .setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.text_sure) { dialog, _ ->
                    run {
                        dialog.dismiss()
                        BaseApplication.INSTANCE.quitApp()
                        startActivity(Intent(context, LoginActivity::class.java))
                    }
                }
                .create()
                .show()
    }

    companion object {

        fun newInstance(cid: Int): PublicAccountFragment {
            return PublicAccountFragment().apply {
                val bundle = Bundle()
                bundle.putInt(Constants.KNOWLEDGE_CID, cid)
                arguments = bundle
            }
        }

    }
}