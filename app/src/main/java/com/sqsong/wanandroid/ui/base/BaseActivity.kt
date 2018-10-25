package com.sqsong.wanandroid.ui.base

import android.annotation.SuppressLint
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.mvp.IPresenter
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.util.SnackbarUtil
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@SuppressLint("Registered")
open abstract class BaseActivity<P : IPresenter<*>> : DaggerAppCompatActivity(), IAppCompatActivity, IView {

    @Inject
    lateinit var mPresenter: P

    override fun beforeInflateView() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showMessage(message: String?) {
        SnackbarUtil.showNormalToast(this, message!!)
    }
}
