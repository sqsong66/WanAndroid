package com.sqsong.wanandroid.util.zxing

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Message
import com.google.zxing.Result
import com.google.zxing.ResultPoint
import com.google.zxing.ResultPointCallback
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.zxing.camera.CameraManager

class CaptureHandler constructor(private val cameraManager: CameraManager,
                                 private val resultListener: ScanningResultListener?) : Handler(), ResultPointCallback {

    private enum class State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    private var mState = State.SUCCESS
    private val mDecodeThread = DecodeThread(this, cameraManager, this)

    init {
        mDecodeThread.start()
        cameraManager.startPreview()
        restartPreviewAndDecode()
    }

    override fun handleMessage(msg: Message?) {
        when (msg?.what) {
            R.id.restart_preview -> restartPreviewAndDecode()
            R.id.decode_succeeded -> {
                mState = State.SUCCESS
                val bundle = msg.data
                var barcode: Bitmap? = null
                var scaleFactor = 1.0f
                if (bundle != null) {
                    val compressedBitmap = bundle!!.getByteArray(DecodeThread.BARCODE_BITMAP)
                    if (compressedBitmap != null) {
                        barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap!!.size, null)
                        barcode = barcode!!.copy(Bitmap.Config.ARGB_8888, true)
                    }
                    scaleFactor = bundle!!.getFloat(DecodeThread.BARCODE_SCALED_FACTOR)
                }
                resultListener?.handleDecode(msg.obj as Result, barcode, scaleFactor)
            }
            R.id.decode_failed -> {
                mState = State.PREVIEW
                cameraManager.requestPreviewFrame(mDecodeThread.getHandler(), R.id.decode)
            }
        }
    }

    override fun foundPossibleResultPoint(points: ResultPoint?) {
        resultListener?.foundPossibleResultPoint(points)
    }

    private fun restartPreviewAndDecode() {
        if (mState == State.SUCCESS) {
            mState = State.PREVIEW
            cameraManager.requestPreviewFrame(mDecodeThread.getHandler(), R.id.decode)
        }
    }

    fun quitSynchronously() {
        mState = State.DONE
        cameraManager.stopPreview()
        val quit = Message.obtain(mDecodeThread.getHandler(), R.id.quit)
        quit.sendToTarget()
        try {
            // Wait at most half a second; should be enough time, and onPause() will timeout quickly
            mDecodeThread.join(500L)
        } catch (e: InterruptedException) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded)
        removeMessages(R.id.decode_failed)
    }
}