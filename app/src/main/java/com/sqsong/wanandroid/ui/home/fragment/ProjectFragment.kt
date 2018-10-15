package com.sqsong.wanandroid.ui.home.fragment

import android.view.View
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.home.mvp.HomePresenter
import javax.inject.Inject

class ProjectFragment @Inject constructor() : BaseFragment<HomePresenter>() {

    override fun getLayoutResId(): Int {
        return R.layout.layout_default_error //fragment_project
    }

    override fun initView(view: View) {

    }

    override fun initEvent() {

    }

}
