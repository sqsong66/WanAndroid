package com.sqsong.wanandroid.ui.home.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.home.mvp.home.HomePresenter
import javax.inject.Inject

class ProjectFragment @Inject constructor() : BaseFragment<HomePresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun getLayoutResId(): Int {
        return R.layout.layout_default_error //fragment_project
    }

    override fun initView(view: View) {

    }

    override fun initEvent() {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_project, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
