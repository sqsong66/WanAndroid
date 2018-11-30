package com.sqsong.wanandroid.ui.scan

import android.content.Context
import android.content.Intent
import android.view.SurfaceView
import com.google.zxing.ResultPoint
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.data.ScanResult
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.scan.fragment.ScanningResultDialog
import com.sqsong.wanandroid.ui.scan.mvp.ScanningContract
import com.sqsong.wanandroid.ui.scan.mvp.ScanningPresenter
import com.sqsong.wanandroid.ui.web.WebViewActivity
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.zxing.camera.CameraManager
import kotlinx.android.synthetic.main.activity_scanning.*

@ChangeThemeAnnotation
class ScanningActivity : BaseActivity<ScanningPresenter>(), ScanningContract.View, ScanningResultDialog.ScanResultDialogActionListener {

    override fun getLayoutResId(): Int = R.layout.activity_scanning

    override fun initEvent() {
        mPresenter.onAttach(this)
    }

    override fun getAppContext(): Context = this

    override fun getSurfaceView(): SurfaceView = surfaceView

    override fun setViewCameraManager(manager: CameraManager) = scanningView.setCameraManager(manager)

    override fun drawPossiblePoint(point: ResultPoint?) = scanningView.addPossibleResultPoint(point)

    override fun showScanResultDialog(scanResult: ScanResult) = ScanningResultDialog.newInstance(scanResult).show(supportFragmentManager, "")

    override fun onDialogDismiss() {
        mPresenter.restartScanning()
    }

    override fun openBrowser(url: String?) {
        if (url == null) return
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(Constants.KEY_WEB_URL, url)
        intent.putExtra(Constants.KEY_WEB_TITLE, getString(R.string.text_website))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        mPresenter.onPause()
        super.onPause()
    }

}