package com.sqsong.wanandroid.ui.scan.mvp

import android.content.Context
import android.view.SurfaceView
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.util.zxing.camera.CameraManager

interface ScanningContract {

    interface View : IView {
        fun getSurfaceView(): SurfaceView
        fun getAppContext(): Context
        fun setViewCameraManager(manager: CameraManager)
    }

}