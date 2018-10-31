package com.sqsong.wanandroid.ui.home.fragment

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.NoLeakHandler
import com.sqsong.wanandroid.common.RecyclerScrollListener
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.ui.base.LazyLoadFragment
import com.sqsong.wanandroid.ui.home.adapter.ProjectAdapter
import com.sqsong.wanandroid.ui.home.adapter.ProjectPopAdapter
import com.sqsong.wanandroid.ui.home.mvp.project.ProjectContract
import com.sqsong.wanandroid.ui.home.mvp.project.ProjectPresenter
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.fragment_project.*
import javax.inject.Inject

class ProjectFragment @Inject constructor() : LazyLoadFragment<ProjectPresenter>(), ProjectContract.View, OnItemClickListener<KnowledgeData>, RecyclerScrollListener.OnLoadMoreListener {

    private var mSwitchPopupWindow: PopupWindow? = null
    private lateinit var mRecyclerScroller: RecyclerScrollListener

    private val mHandler: NoLeakHandler<ProjectFragment> by lazy {
        NoLeakHandler(this)
    }

    private val mLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {
                        mPresenter.loadInitData()
                    }
                }).build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_project
    }

    override fun initEvent() {
        setHasOptionsMenu(true)
        setupRecyclerView()
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener { mPresenter.refreshData() }
        mPresenter.onAttach(this)
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = mLayoutManager
        mRecyclerScroller = RecyclerScrollListener(mLayoutManager)
        recycler.addOnScrollListener(mRecyclerScroller)
        mRecyclerScroller.setOnLoadMoreListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_project, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_project) {
            mPresenter.checkPopState()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onLoadMore() {
        mPresenter.loadMore()
    }

    override fun loadInitData() = mPresenter.loadInitData()

    override fun getFragmentContext(): Context = context!!

    override fun setRecyclerAdapter(adapter: ProjectAdapter) {
        recycler.adapter = adapter
    }

    override fun findRecyclerLastVisibleItemPosition(): Int = mLayoutManager.findLastVisibleItemPosition()

    override fun getHandler(): Handler = mHandler

    override fun loadFinish() = mRecyclerScroller.loadFinish()

    override fun scrollRecycler(position: Int) = recycler.smoothScrollToPosition(position)

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

    override fun showTitle(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            (activity as AppCompatActivity).toolbar.title = title
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun preparePopupWindow(dataList: List<KnowledgeData>, forceReCreate: Boolean) {
        val recyclerView = layoutInflater.inflate(R.layout.pop_project, null) as RecyclerView
        val adapter = ProjectPopAdapter(context!!, dataList)
        adapter.setOnItemClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        mSwitchPopupWindow = PopupWindow(recyclerView)
        mSwitchPopupWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mSwitchPopupWindow?.height = DensityUtil.getScreenHeight() / 2
        mSwitchPopupWindow?.isOutsideTouchable = true
        mSwitchPopupWindow?.elevation = DensityUtil.dip2px(10).toFloat()
        mSwitchPopupWindow?.setBackgroundDrawable(ColorDrawable(CommonUtil.getThemeColor(context!!, R.attr.colorPrimary)))
    }

    override fun showPopupWindow(classifyList: MutableList<KnowledgeData>) {
        if (mSwitchPopupWindow == null) preparePopupWindow(classifyList, true)
        if (mSwitchPopupWindow?.isShowing == true) mSwitchPopupWindow?.dismiss()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSwitchPopupWindow?.showAsDropDown((activity as AppCompatActivity).toolbar, -DensityUtil.dip2px(5), 0, Gravity.END)
        } else {
            mSwitchPopupWindow?.showAtLocation((activity as AppCompatActivity).toolbar, Gravity.BOTTOM, -DensityUtil.dip2px(5), 0)
        }
    }

    override fun onItemClick(data: KnowledgeData?, position: Int) {
        mSwitchPopupWindow?.dismiss()
        showTitle(data?.name)
        mPresenter.refreshData(data)
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

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}

