package com.sqsong.wanandroid.ui.scan

import android.content.Context
import android.view.SurfaceView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.scan.mvp.ScanningContract
import com.sqsong.wanandroid.ui.scan.mvp.ScanningPresenter
import com.sqsong.wanandroid.util.zxing.camera.CameraManager
import kotlinx.android.synthetic.main.activity_scanning.*

@ChangeThemeAnnotation
class ScanningActivity : BaseActivity<ScanningPresenter>(), ScanningContract.View {

    override fun getLayoutResId(): Int = R.layout.activity_scanning

    override fun initEvent() {
        mPresenter.onAttach(this)
    }

    override fun getAppContext(): Context = this

    override fun getSurfaceView(): SurfaceView = surfaceView

    override fun setViewCameraManager(manager: CameraManager) {

    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

}