package com.sqsong.wanandroid.ui.wechat.fragment

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.LazyLoadFragment
import com.sqsong.wanandroid.ui.wechat.mvp.AccountContract
import com.sqsong.wanandroid.ui.wechat.mvp.AccountPresenter
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.fragment_home_backup.*

class PublicAccountFragment : LazyLoadFragment<AccountPresenter>(), AccountContract.View {

    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(recycler)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {

                    }
                }).build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_backup
    }

    override fun initEvent() {

    }

    override fun loadInitData() {

    }

    override fun showEmptyPage() = mPageLayout.showEmptyLayout()

    override fun showLoadingPage() = mPageLayout.showLoadingLayout()

    override fun showContentPage() = mPageLayout.showContentLayout()

    override fun showErrorPage() = mPageLayout.showErrorLayout()

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}