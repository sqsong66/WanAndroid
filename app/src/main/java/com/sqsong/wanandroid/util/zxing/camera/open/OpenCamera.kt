package com.sqsong.wanandroid.util.zxing.camera.open

import android.hardware.Camera

data class OpenCamera(val index: Int, val camera: Camera, val facing: CameraFacing, val orientation: Int)