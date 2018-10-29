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
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.ui.base.LazyLoadFragment
import com.sqsong.wanandroid.ui.home.activity.KnowledgeActivity
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeAdapter
import com.sqsong.wanandroid.ui.home.mvp.knowledge.KnowledgeContract
import com.sqsong.wanandroid.ui.home.mvp.knowledge.KnowledgePresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.fragment_knowledge.*
import javax.inject.Inject

class KnowledgeFragment @Inject constructor() : LazyLoadFragment<KnowledgePresenter>(), KnowledgeContract.View {

    private var mSwitchPopupWindow: PopupWindow? = null

    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {
                        mPresenter.requestKnowledgeData()
                    }
                }).build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_knowledge
    }

    override fun initEvent() {
        setHasOptionsMenu(true)
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener { loadInitData() }
        mPresenter.onAttach(this)
    }

    override fun loadInitData() {
        mPresenter.requestKnowledgeData()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_knowledge_navigation, menu)
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

    override fun setRecyclerAdapter(adapter: KnowledgeAdapter) {
        recycler.layoutManager = LinearLayoutManager(context)
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

    override fun startKnowledgeActivity(data: KnowledgeData?) {
        val intent = Intent(context, KnowledgeActivity::class.java)
        intent.putExtra(Constants.KNOWLEDGE_DATA, data)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
