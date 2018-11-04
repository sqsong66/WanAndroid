package com.sqsong.wanandroid.ui.wechat

import android.view.MenuItem
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.wechat.mvp.PublicAccountContract
import com.sqsong.wanandroid.ui.wechat.mvp.PublicAccountPresenter
import com.sqsong.wanandroid.util.ext.setupToolbar
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.activity_public_account.*

@ChangeThemeAnnotation
class PublicAccountActivity : BaseActivity<PublicAccountPresenter>(), PublicAccountContract.View {


    private val mPageLayout: DefaultPageLayout by lazy {
        DefaultPageLayout.Builder(this)
                .setTargetPage(viewPager)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {

                    }
                }).build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_public_account
    }

    override fun initEvent() {
        setupToolbar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
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