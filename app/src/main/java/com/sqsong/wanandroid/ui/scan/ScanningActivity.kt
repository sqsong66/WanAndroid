package com.sqsong.wanandroid.ui.scan

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.scan.mvp.ScanningContract
import com.sqsong.wanandroid.ui.scan.mvp.ScanningPresenter

class ScanningActivity : BaseActivity<ScanningPresenter>(), ScanningContract.View {

    override fun getLayoutResId(): Int = R.layout.activity_scanning

    override fun initEvent() {
    }

}