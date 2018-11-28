package com.sqsong.wanandroid.util.zxing

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.LogUtil
import com.sqsong.wanandroid.util.zxing.camera.CameraManager
import java.io.ByteArrayOutputStream

class DecodeHandler constructor(private val captureHandler: CaptureHandler,
                                private val cameraManager: CameraManager,
                                hints: MutableMap<DecodeHintType, Any>) : Handler() {

    private var isRunning = true
    private val mMultiFormatReader: MultiFormatReader = MultiFormatReader()

    init {
        mMultiFormatReader.setHints(hints)
    }

    override fun handleMessage(msg: Message?) {
        if (msg == null || !isRunning) {
            return
        }
        when (msg.what) {
            R.id.decode -> decode(msg.obj as ByteArray, msg.arg1, msg.arg2)
            R.id.quit -> {
                isRunning = false
                Looper.myLooper()!!.quit()
            }
        }
    }

    private fun decode(data: ByteArray, width: Int, height: Int) {
        val start = System.currentTimeMillis()
        var rawResult: Result? = null
        val source = cameraManager.buildLuminanceSource(data, width, height)
        if (source != null) {
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                rawResult = mMultiFormatReader.decodeWithState(bitmap)
            } catch (re: ReaderException) {
                re.printStackTrace()
            } finally {
                mMultiFormatReader.reset()
            }
        }

        if (rawResult != null) {
            // Don't log the barcode contents for security.
            val end = System.currentTimeMillis()
            LogUtil.d("DecodeHandler", "Found barcode in " + (end - start) + " ms")

            val message = Message.obtain(captureHandler, R.id.decode_succeeded, rawResult)
            val bundle = Bundle()
            bundleThumbnail(source!!, bundle)
            message.data = bundle
            message.sendToTarget()
        } else {
            val message = Message.obtain(captureHandler, R.id.decode_failed)
            message.sendToTarget()
        }
    }

    private fun bundleThumbnail(source: PlanarYUVLuminanceSource, bundle: Bundle) {
        val pixels = source.renderThumbnail()
        val width = source.thumbnailWidth
        val height = source.thumbnailHeight
        val bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888)
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray())
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, width.toFloat() / source.width)
    }
}