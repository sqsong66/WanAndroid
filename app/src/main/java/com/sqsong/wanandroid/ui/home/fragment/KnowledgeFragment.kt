package com.sqsong.wanandroid.ui.home.fragment

import android.view.View
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.home.mvp.home.HomePresenter
import javax.inject.Inject

class KnowledgeFragment @Inject constructor() : BaseFragment<HomePresenter>() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_knowledge
    }

    override fun initView(view: View) {

    }

    override fun initEvent() {

    }

}
