package com.sqsong.wanandroid.ui.scan.mvp

import android.content.Context
import android.graphics.Bitmap
import android.os.Vibrator
import android.view.SurfaceHolder
import com.google.zxing.Result
import com.google.zxing.ResultPoint
import com.google.zxing.client.result.ResultParser
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.ScanResult
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.zxing.CaptureHandler
import com.sqsong.wanandroid.util.zxing.ScanningResultListener
import com.sqsong.wanandroid.util.zxing.camera.CameraManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ScanningPresenter @Inject constructor(private val disposable: CompositeDisposable) :
        BasePresenter<ScanningContract.View, IModel>(null, disposable), ScanningResultListener, SurfaceHolder.Callback {


    private var hasSurface = false
    private var mVibrator: Vibrator? = null
    private var mCaptureHandler: CaptureHandler? = null
    private lateinit var mCameraManager: CameraManager/* by lazy {
        CameraManager(mView.getAppContext().applicationContext)
    }*/

    override fun onAttach(view: ScanningContract.View) {
        super.onAttach(view)
        mVibrator = mView.getAppContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        disposable.add(lightClickDisposable())
    }

    private fun lightClickDisposable(): Disposable {
        return RxView.clicks(mView.lightImage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    lighting(mView.lightImage().isChecked)
                }
    }

    fun onResume() {
        mCameraManager = CameraManager(mView.getAppContext().applicationContext)
        mView.setViewCameraManager(mCameraManager)

        val surfaceHolder = mView.getSurfaceView().holder
        if (hasSurface) {
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(this)
        }
    }

    fun onPause() {
        mCaptureHandler?.quitSynchronously()
        mCaptureHandler = null

        mCameraManager.closeDriver()
        if (!hasSurface) {
            mView.getSurfaceView().holder.removeCallback(this)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (holder == null) return
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        // do nothing.
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        hasSurface = false
    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        if (mCameraManager.isOpen()) return

        try {
            mCameraManager.openDriver(surfaceHolder)
            if (mCaptureHandler == null) {
                mCaptureHandler = CaptureHandler(mCameraManager, this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun foundPossibleResultPoint(point: ResultPoint?) {
        mView.drawPossiblePoint(point)
    }

    override fun handleDecode(result: Result, barcodeBitmap: Bitmap?, scaleFactor: Float) {
        val resultText = result.text
        val typeText = ResultParser.parseResult(result).type.toString()
        val formatText = result.barcodeFormat.toString()
        val timeText = CommonUtil.formatLongTimeToString(result.timestamp, "yyyy-MM-dd HH:mm")
        var bmpBytes: ByteArray? = null
        if (barcodeBitmap != null) {
            val stream = ByteArrayOutputStream()
            barcodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            bmpBytes = stream.toByteArray()
        }
        mView.showScanResultDialog(ScanResult(resultText, formatText, typeText, timeText, bmpBytes))
        CommonUtil.playDefaultSound(mView.getAppContext().applicationContext)
        if (mVibrator?.hasVibrator() == true) {
            mVibrator?.vibrate(10)
        }
    }

    fun restartScanning() = mCaptureHandler?.sendEmptyMessageDelayed(R.id.restart_preview, 500)

    private fun lighting(light: Boolean) {
        mCameraManager.setTorch(light)
    }

}