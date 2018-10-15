package com.sqsong.wanandroid.ui.home.fragment

import android.view.View
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.HomeBannerData
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.home.mvp.HomeContract
import com.sqsong.wanandroid.ui.home.mvp.HomePresenter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_news.*
import javax.inject.Inject

class HomeFragment @Inject constructor() : BaseFragment<HomePresenter>(), HomeContract.HomeView, View.OnClickListener {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(view: View) {
        starIv.setOnClickListener(this)
    }

    override fun initEvent() {
        mPresenter.onAttach(this)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) bannerView?.startLoop() else bannerView?.stopLoop()
    }

    override fun onClick(v: View?) {
        // SnackbarUtil.showSnackText(v!!, "Star")
    }

    override fun showHomeBanner(bannerData: MutableList<HomeBannerData>) {
        bannerView.setBannerData(bannerData)
    }

}
