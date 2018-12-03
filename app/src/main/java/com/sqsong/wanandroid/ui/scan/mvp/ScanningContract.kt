package com.sqsong.wanandroid.ui.scan.mvp

import android.content.Context
import android.view.SurfaceView
import com.google.zxing.ResultPoint
import com.sqsong.wanandroid.data.ScanResult
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.util.zxing.camera.CameraManager
import com.sqsong.wanandroid.view.CheckableImageView

interface ScanningContract {

    interface View : IView {
        fun getSurfaceView(): SurfaceView
        fun getAppContext(): Context
        fun setViewCameraManager(manager: CameraManager)
        fun drawPossiblePoint(point: ResultPoint?)
        fun showScanResultDialog(scanResult: ScanResult)
        fun lightImage(): CheckableImageView
    }

}