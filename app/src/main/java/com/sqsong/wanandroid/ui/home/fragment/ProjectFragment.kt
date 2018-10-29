package com.sqsong.wanandroid.ui.home.fragment

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.ui.base.LazyLoadFragment
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeAdapter
import com.sqsong.wanandroid.ui.home.mvp.project.ProjectContract
import com.sqsong.wanandroid.ui.home.mvp.project.ProjectPresenter
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.fragment_project.*
import javax.inject.Inject

class ProjectFragment @Inject constructor() : LazyLoadFragment<ProjectPresenter>(), ProjectContract.View {

    private var mSwitchPopupWindow: PopupWindow? = null

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
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener { loadInitData() }
        mPresenter.onAttach(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_project, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_project) {

        }
        return super.onOptionsItemSelected(item)
    }

    override fun loadInitData() = mPresenter.loadInitData()

    override fun getFragmentContext(): Context = context!!

    override fun setRecyclerAdapter(adapter: KnowledgeAdapter) {

    }

    override fun scrollRecycler(position: Int) {

    }

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun preparePopupWindow(dataList: List<KnowledgeData>, forceReCreate: Boolean) {
        val recyclerView = layoutInflater.inflate(R.layout.layout_knowledge_pop, null) as RecyclerView


        mSwitchPopupWindow = PopupWindow(view)
        mSwitchPopupWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mSwitchPopupWindow?.height = DensityUtil.getScreenHeight() / 2
        mSwitchPopupWindow?.isOutsideTouchable = true
        mSwitchPopupWindow?.elevation = DensityUtil.dip2px(10).toFloat()
        mSwitchPopupWindow?.setBackgroundDrawable(ColorDrawable(CommonUtil.getThemeColor(context!!, R.attr.colorPrimary)))
    }
}
