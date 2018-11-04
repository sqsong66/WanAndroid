package com.sqsong.wanandroid.ui.home.fragment

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.LazyLoadInjectFragment
import com.sqsong.wanandroid.ui.home.adapter.NavigationAdapter
import com.sqsong.wanandroid.ui.home.mvp.navigation.NavigationContract
import com.sqsong.wanandroid.ui.home.mvp.navigation.NavigationPresenter
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.fragment_navigation.*
import javax.inject.Inject

class NavigationFragment @Inject constructor() : LazyLoadInjectFragment<NavigationPresenter>(), NavigationContract.View {

    private var mSwitchPopupWindow: PopupWindow? = null

    private val mPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {
                        loadInitData()
                    }
                })
                .build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_navigation
    }

    override fun initEvent() {
        setHasOptionsMenu(true)
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener { loadInitData() }
        mPresenter.onAttach(this)
    }

    override fun loadInitData() {
        mPresenter.requestNavigationList()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            (activity as AppCompatActivity).toolbar.title = getString(R.string.text_navigation)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        // inflater?.inflate(R.menu.menu_knowledge_navigation, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_switch) {
            showSwitchPop()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun showSwitchPop() {
        if (mSwitchPopupWindow == null) {
            val view = layoutInflater.inflate(R.layout.layout_knowledge_pop, null)
            mSwitchPopupWindow = PopupWindow(view)
            mSwitchPopupWindow?.width = DensityUtil.getScreenWidth() / 2
            mSwitchPopupWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            mSwitchPopupWindow?.isOutsideTouchable = true
            mSwitchPopupWindow?.elevation = DensityUtil.dip2px(10).toFloat()
            mSwitchPopupWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        if (mSwitchPopupWindow?.isShowing!!) {
            mSwitchPopupWindow?.dismiss()
        } else {
            mSwitchPopupWindow?.showAsDropDown((activity as AppCompatActivity).toolbar, -DensityUtil.dip2px(10), 0, Gravity.END)
        }
    }

    override fun getFragmentContext(): Context {
        return context!!
    }

    override fun setRecyclerAdapter(adapter: NavigationAdapter, itemDecoration: RecyclerView.ItemDecoration) {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.addItemDecoration(itemDecoration)
        recycler.adapter = adapter
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

    override fun showErrorPage() {
        mPageLayout.showErrorLayout()
    }

    override fun scrollRecycler(position: Int) {
        recycler.smoothScrollToPosition(position)
    }

    override fun startNewActivity(intent: Intent) {
        startActivity(intent)
    }

    override fun getRecycler(): RecyclerView {
        return recycler
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
