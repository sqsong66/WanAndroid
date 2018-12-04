package com.sqsong.wanandroid.util.zxing.camera

import android.content.Context
import android.graphics.Point
import android.hardware.Camera
import android.view.Surface
import android.view.WindowManager
import com.sqsong.wanandroid.util.LogUtil
import com.sqsong.wanandroid.util.zxing.camera.open.CameraFacing
import com.sqsong.wanandroid.util.zxing.camera.open.OpenCamera

class CameraConfigurationManager constructor(private val context: Context?) {

    private var cwNeededRotation = 0
    private var cwRotationFromDisplayToCamera = 0
    private lateinit var screenResolution: Point
    private lateinit var cameraResolution: Point
    private lateinit var bestPreviewSize: Point
    private lateinit var previewSizeOnScreen: Point

    fun initFromCameraParameters(camera: OpenCamera) {
        val parameters = camera.camera.parameters
        val manager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay

        val orientation = display.orientation
        var cwRotationFromNaturalToDisplay = when (orientation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> {
                if (orientation % 90 == 0) {
                    (360 + orientation) % 360
                } else {
                    throw IllegalArgumentException("Bad rotation: $orientation")
                }
            }
        }
        LogUtil.i(TAG, "Display orientation: $cwRotationFromNaturalToDisplay")

        var cwRotationFromNaturalToCamera = camera.orientation
        LogUtil.i(TAG, "Camera orientation: $cwRotationFromNaturalToCamera")

        if (camera.facing == CameraFacing.FRONT) {
            cwRotationFromNaturalToCamera = (360 - cwRotationFromNaturalToCamera) % 360
            LogUtil.i(TAG, "Front camera overriden to: $cwRotationFromNaturalToCamera")
        }

        cwRotationFromDisplayToCamera = (360 + cwRotationFromNaturalToCamera - cwRotationFromNaturalToDisplay) % 360
        LogUtil.i(TAG, "Final display orientation: $cwRotationFromDisplayToCamera")
        cwNeededRotation = if (camera.facing === CameraFacing.FRONT) {
            LogUtil.i(TAG, "Compensating rotation for front camera")
            (360 - cwRotationFromDisplayToCamera) % 360
        } else {
            cwRotationFromDisplayToCamera
        }
        LogUtil.i(TAG, "Clockwise rotation from display to camera: $cwNeededRotation")

        val theScreenResolution = Point()
        display.getSize(theScreenResolution)
        screenResolution = theScreenResolution
        LogUtil.i(TAG, "Screen resolution in current orientation: $screenResolution")
        cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution)
        LogUtil.i(TAG, "Camera resolution: $cameraResolution")
        bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution)
        LogUtil.i(TAG, "Best available preview size: $bestPreviewSize")

        val isScreenPortrait = screenResolution.x < screenResolution.y
        val isPreviewSizePortrait = bestPreviewSize.x < bestPreviewSize.y

        previewSizeOnScreen = if (isScreenPortrait == isPreviewSizePortrait) {
            bestPreviewSize
        } else {
            Point(bestPreviewSize.y, bestPreviewSize.x)
        }
        LogUtil.i(TAG, "Preview size on screen: $previewSizeOnScreen")
    }

    fun setDesiredCameraParameters(camera: OpenCamera, safeMode: Boolean) {
        val theCamera = camera.camera
        val parameters = theCamera.parameters
        if (parameters == null) {
            LogUtil.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.")
            return
        }

        LogUtil.i(TAG, "Initial camera parameters: " + parameters!!.flatten())

        if (safeMode) {
            LogUtil.w(TAG, "In camera config safe mode -- most settings will not be honored")
        }

        // initializeTorch(parameters, safeMode)

        CameraConfigurationUtils.setFocus(parameters, true, true, safeMode)

        if (!safeMode) {
            /*if (prefs.getBoolean(PreferencesActivity.KEY_INVERT_SCAN, false)) {
                CameraConfigurationUtils.setInvertColor(parameters)
            }

            if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_BARCODE_SCENE_MODE, true)) {
                CameraConfigurationUtils.setBarcodeSceneMode(parameters)
            }

            if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_METERING, true)) {
                CameraConfigurationUtils.setVideoStabilization(parameters)
                CameraConfigurationUtils.setFocusArea(parameters)
                CameraConfigurationUtils.setMetering(parameters)
            }*/
            //SetRecordingHint to true also a workaround for low framerate on Nexus 4
            //https://stackoverflow.com/questions/14131900/extreme-camera-lag-on-nexus-4
            parameters.setRecordingHint(true)
        }

        parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y)
        theCamera.parameters = parameters
        theCamera.setDisplayOrientation(cwRotationFromDisplayToCamera)
        val afterParameters = theCamera.parameters
        val afterSize = afterParameters.previewSize
        if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
            LogUtil.w(TAG, "Camera said it supported preview size " + bestPreviewSize.x + 'x'.toString() + bestPreviewSize.y +
                    ", but after setting it, preview size is " + afterSize.width + 'x'.toString() + afterSize.height)
            bestPreviewSize.x = afterSize.width
            bestPreviewSize.y = afterSize.height
        }
    }

    fun getBestPreviewSize(): Point {
        return bestPreviewSize
    }

    fun getPreviewSizeOnScreen(): Point {
        return previewSizeOnScreen
    }

    fun getCameraResolution(): Point {
        return cameraResolution
    }

    fun getScreenResolution(): Point {
        return screenResolution
    }

    fun getCWNeededRotation(): Int {
        return cwNeededRotation
    }

    fun getTorchState(camera: Camera?): Boolean {
        if (camera != null) {
            val parameters = camera.parameters
            if (parameters != null) {
                val flashMode = parameters.flashMode
                return Camera.Parameters.FLASH_MODE_ON == flashMode || Camera.Parameters.FLASH_MODE_TORCH == flashMode
            }
        }
        return false
    }

    fun setTorch(camera: Camera, newSetting: Boolean) {
        val parameters = camera.parameters
        doSetTorch(parameters, newSetting, false)
        camera.parameters = parameters
    }

    private fun initializeTorch(parameters: Camera.Parameters,/* prefs: SharedPreferences, */safeMode: Boolean) {
        val currentSetting = FrontLightMode.readPref(FrontLightMode.ON.toString()) === FrontLightMode.ON
        doSetTorch(parameters, currentSetting, safeMode)
    }

    private fun doSetTorch(parameters: Camera.Parameters, newSetting: Boolean, safeMode: Boolean) {
        CameraConfigurationUtils.setTorch(parameters, newSetting)
        // val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        if (!safeMode/* && !prefs.getBoolean(PreferencesActivity.KEY_DISABLE_EXPOSURE, true)*/) {
            CameraConfigurationUtils.setBestExposure(parameters, newSetting)
        }
    }

    companion object {
        const val TAG = "CameraConfigurationManager"
    }

}