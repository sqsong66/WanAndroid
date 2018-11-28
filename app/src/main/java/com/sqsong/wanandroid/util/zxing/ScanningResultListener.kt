package com.sqsong.wanandroid.util.zxing

import android.graphics.Bitmap
import com.google.zxing.Result
import com.google.zxing.ResultPoint

interface ScanningResultListener {

    fun foundPossibleResultPoint(points: ResultPoint?)

    fun handleDecode(result: Result, barcodeBitmap: Bitmap?, scaleFactor: Float)
}