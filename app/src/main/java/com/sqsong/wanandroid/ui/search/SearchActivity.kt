package com.sqsong.wanandroid.ui.search

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.NoLeakHandler
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.ui.search.adapter.SearchAdapter
import com.sqsong.wanandroid.ui.search.mvp.SearchContract
import com.sqsong.wanandroid.ui.search.mvp.SearchPresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.SnackbarUtil
import com.sqsong.wanandroid.view.DefaultPageLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity<SearchPresenter>(), SearchContract.View, RecyclerScrollListener.OnLoadMoreListener, View.OnClickListener {

    private var mSearchKey: String? = null
    private lateinit var mRecyclerScroller: RecyclerScrollListener

    private val mHandler: NoLeakHandler<SearchActivity> by lazy {
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
                        mPresenter.refreshData()
                    }
                }).build()
    }

    override fun beforeInflateView() {
        mSearchKey = intent.getStringExtra(Constants.SEARCH_KEY)
    }

    override fun getLayoutResId(): Int = R.layout.activity_search

    override fun initEvent() {
        setupRecycler()
        searchEdit.setText(mSearchKey)
        searchEdit.setSelection(searchEdit.text.toString().length)
        backIv.setOnClickListener(this)
        clearIv.setOnClickListener(this)
        mPresenter.onAttach(this)
    }

    private fun setupRecycler() {
        recycler.layoutManager = mLayoutManager
        mRecyclerScroller = RecyclerScrollListener(mLayoutManager)
        mRecyclerScroller.setOnLoadMoreListener(this)
        recycler.addOnScrollListener(mRecyclerScroller)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> finish()
            R.id.clearIv -> searchEdit.text = null
        }
    }

    override fun showClear(show: Boolean) {
        if (show) {
            if (clearIv.visibility == View.GONE) {
                clearIv.visibility = View.VISIBLE
            }
        } else {
            if (clearIv.visibility == View.VISIBLE) {
                clearIv.visibility = View.GONE
            }
        }
    }

    override fun searchObservable(): Observable<CharSequence> = RxTextView.textChanges(searchEdit)

    override fun onLoadMore() = mPresenter.loadMore()

    override fun getInitKey(): String? = mSearchKey

    override fun showEmptyPage() = mPageLayout.showEmptyLayout()

    override fun showLoadingPage() = mPageLayout.showLoadingLayout()

    override fun showContentPage() = mPageLayout.showContentLayout()

    override fun showErrorPage() = mPageLayout.showErrorLayout()

    override fun getAppContext(): Context = this

    override fun setRecyclerAdapter(adapter: SearchAdapter) {
        recycler.adapter = adapter
    }

    override fun getHandler(): Handler = mHandler

    override fun findRecyclerLastVisibleItemPosition(): Int = mLayoutManager.findLastVisibleItemPosition()

    override fun loadFinish() = mRecyclerScroller.loadFinish()

    override fun showMessage(message: String?) {
        SnackbarUtil.showToastText(this, message)
    }

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

    override fun startNewActivity(intent: Intent) = startActivity(intent)

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}