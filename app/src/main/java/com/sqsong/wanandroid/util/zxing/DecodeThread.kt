package com.sqsong.wanandroid.util.zxing

import android.os.Handler
import android.os.Looper
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.ResultPointCallback
import com.sqsong.wanandroid.util.zxing.camera.CameraManager
import java.util.*
import java.util.concurrent.CountDownLatch

class DecodeThread(private val captureHandler: CaptureHandler,
                   private val cameraManager: CameraManager,
                   resultPointCallback: ResultPointCallback) : Thread() {

    private lateinit var mHandler: Handler
    private var hints: MutableMap<DecodeHintType, Any> = EnumMap(DecodeHintType::class.java)
    private val mCountDownLatch: CountDownLatch = CountDownLatch(1)

    init {
        val decodeFormats = EnumSet.noneOf(BarcodeFormat::class.java)
        decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS)
        decodeFormats.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS)
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS)
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS)
        decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS)
        decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS)

        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
        hints[DecodeHintType.NEED_RESULT_POINT_CALLBACK] = resultPointCallback
    }

    fun getHandler(): Handler {
        try {
            mCountDownLatch.await()
        } catch (ie: InterruptedException) {
            ie.printStackTrace()
        }
        return mHandler
    }

    override fun run() {
        Looper.prepare()
        mHandler = DecodeHandler(captureHandler, cameraManager, hints)
        mCountDownLatch.countDown()
        Looper.loop()
    }

    companion object {
        const val BARCODE_BITMAP = "barcode_bitmap"
        const val BARCODE_SCALED_FACTOR = "barcode_scaled_factor"
    }

}