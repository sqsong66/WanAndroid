package com.sqsong.wanandroid.ui.home.fragment

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.ui.base.LazyLoadFragment
import com.sqsong.wanandroid.ui.home.activity.KnowledgeActivity
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeAdapter
import com.sqsong.wanandroid.ui.home.mvp.knowledge.KnowledgeContract
import com.sqsong.wanandroid.ui.home.mvp.knowledge.KnowledgePresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.fragment_knowledge.*
import javax.inject.Inject

class KnowledgeFragment @Inject constructor() : LazyLoadFragment<KnowledgePresenter>(), KnowledgeContract.View {

    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {

                    }
                }).build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_knowledge
    }

    override fun initEvent() {
        mPresenter.onAttach(this)
    }

    override fun loadInitData() {
        mPresenter.requestKnowledgeData()
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
        mPageLayout.showContentLayout()
    }

    override fun showErrorPage() {
        mPageLayout.showErrorLayout()
    }

    override fun scrollRecycler(position: Int) {
        recycler.smoothScrollToPosition(position)
    }

    override fun startKnowledgeActivity(data: KnowledgeData?) {
        // SnackbarUtil.showNormalToast(context!!, data?.name!!)
        val intent = Intent(context, KnowledgeActivity::class.java)
        intent.putExtra(Constants.KNOWLEDGE_DATA, data)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
