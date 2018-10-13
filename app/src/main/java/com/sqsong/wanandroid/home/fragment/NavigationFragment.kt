package com.sqsong.wanandroid.home.fragment

import android.view.View
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseFragment
import com.sqsong.wanandroid.util.SnackbarUtil
import com.sqsong.wanandroid.view.DefaultPageLayout
import kotlinx.android.synthetic.main.fragment_navigation_backup.*
import javax.inject.Inject

class NavigationFragment @Inject constructor() : BaseFragment(), View.OnClickListener {


    private val mPageLayout by lazy {
        DefaultPageLayout.Builder(context!!)
                .setTargetPage(contentLl)
                .setOnRetryClickListener(object : DefaultPageLayout.OnRetryClickListener {
                    override fun onRetry() {
                        SnackbarUtil.showSnackText(contentLl, "Retry")
                    }
                })
                .build()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_navigation_backup
    }

    override fun initView(view: View) {

    }

    override fun initEvent() {
        contentBtn.setOnClickListener(this)
        loadingBtn.setOnClickListener(this)
        emptyBtn.setOnClickListener(this)
        errorBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.contentBtn -> mPageLayout.showContentLayout()
            R.id.loadingBtn -> mPageLayout.showLoadingLayout()
            R.id.emptyBtn -> mPageLayout.showEmptyLayout()
            R.id.errorBtn -> mPageLayout.showErrorLayout()
        }
    }

}
