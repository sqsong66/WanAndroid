package com.sqsong.wanandroid.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.zxing.camera.CameraManager

class ScanningView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                             defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var borderGap: Int = 0
    private var muskColor: Int = 0
    private var borderWidth: Int = 0
    private var borderHeight: Int = 0
    private var centerLineHeight: Int = 0
    private var borderColor: Int = Color.WHITE
    private var centerLineColor: Int = Color.WHITE

    private var mStartX: Float = .0f
    private var mStartY: Float = .0f
    private var mRectWidth: Int = 0
    private lateinit var mPaint: Paint
    private var mLineWidth: Int = 0
    private var mLineStartY: Float = .0f
    private var mGapDistance: Float = .0f
    private var mPointRadius: Float = .0f
    private var mCameraManager: CameraManager? = null
    private var mPointList = ArrayList<ResultPoint>(5)
    private var mLastPointList: ArrayList<ResultPoint>? = null

    init {
        initAttrs(context, attrs)
        initParams()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScanningView)
            muskColor = typedArray.getColor(R.styleable.ScanningView_muskColor, ContextCompat.getColor(context, R.color.colorSearchMusk))
            borderColor = typedArray.getColor(R.styleable.ScanningView_bColor, Color.WHITE)
            centerLineColor = typedArray.getColor(R.styleable.ScanningView_centerLineColor, Color.WHITE)
            borderWidth = typedArray.getDimension(R.styleable.ScanningView_bWidth, DensityUtil.dip2px(30).toFloat()).toInt()
            borderHeight = typedArray.getDimension(R.styleable.ScanningView_bHeight, DensityUtil.dip2px(5).toFloat()).toInt()
            centerLineHeight = typedArray.getDimension(R.styleable.ScanningView_centerLineHeight, DensityUtil.dip2px(2).toFloat()).toInt()
            borderGap = typedArray.getDimension(R.styleable.ScanningView_borderGap, DensityUtil.dip2px(2).toFloat()).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray?.recycle()
        }
    }

    private fun initParams() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.isDither = true
        mPaint.style = Paint.Style.FILL

        val screenWidth = DensityUtil.getScreenWidth()
        val screenHeight = DensityUtil.getScreenHeight()
        mRectWidth = screenWidth * 3 / 5
        mStartX = ((screenWidth - mRectWidth) / 2).toFloat()
        mStartY = (screenHeight / 5).toFloat()
        mLineStartY = mStartY
        mLineWidth = mRectWidth

        mGapDistance = (borderHeight + borderGap).toFloat()
        mPointRadius = DensityUtil.dip2px(2).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawMusk(canvas)
        drawBorder(canvas)
        drawCenterLine(canvas)
        drawPossiblePoints(canvas)

        postInvalidateDelayed(ANIMATE_DELAY/*, mStartX.toInt(), mStartY.toInt(),
                (mStartX + mRectWidth).toInt(), (mStartY + mRectWidth).toInt()*/)
    }

    private fun drawMusk(canvas: Canvas?) {
        mPaint.color = muskColor
        canvas?.drawRect(.0f, .0f, width.toFloat(), mStartY, mPaint)
        canvas?.drawRect(.0f, mStartY, mStartX, height.toFloat(), mPaint)
        canvas?.drawRect(mStartX + mRectWidth, mStartY, width.toFloat(), height.toFloat(), mPaint)
        canvas?.drawRect(mStartX, mStartY + mRectWidth, mStartX + mRectWidth, height.toFloat(), mPaint)
    }

    private fun drawBorder(canvas: Canvas?) {
        mPaint.color = borderColor
        // Left top corner
        canvas?.drawRect(mStartX - mGapDistance, mStartY - mGapDistance, mStartX - mGapDistance + borderWidth, mStartY - mGapDistance + borderHeight, mPaint)
        canvas?.drawRect(mStartX - mGapDistance, mStartY - mGapDistance + borderHeight, mStartX - mGapDistance + borderHeight, mStartY - mGapDistance + borderWidth, mPaint)

        // Right top corner
        canvas?.drawRect(mStartX + mRectWidth + mGapDistance - borderWidth, mStartY - mGapDistance, mStartX + mGapDistance + mRectWidth, mStartY - mGapDistance + borderHeight, mPaint)
        canvas?.drawRect(mStartX + mRectWidth + mGapDistance - borderHeight, mStartY - mGapDistance, mStartX + mRectWidth + mGapDistance, mStartY - mGapDistance + borderWidth, mPaint)

        // Left bottom corner
        canvas?.drawRect(mStartX - mGapDistance, mStartY + mRectWidth + mGapDistance - borderWidth, mStartX - mGapDistance + borderHeight, mStartY + mRectWidth + mGapDistance, mPaint)
        canvas?.drawRect(mStartX - mGapDistance, mStartY + mRectWidth + mGapDistance - borderHeight, mStartX + borderWidth - mGapDistance, mStartY + mRectWidth + mGapDistance, mPaint)

        // Right bottom corner
        canvas?.drawRect(mStartX + mRectWidth + mGapDistance - borderHeight, mStartY + mRectWidth + mGapDistance - borderWidth, mStartX + mRectWidth + mGapDistance, mStartY + mRectWidth + mGapDistance, mPaint)
        canvas?.drawRect(mStartX + mRectWidth + mGapDistance - borderWidth, mStartY + mRectWidth + mGapDistance - borderHeight, mStartX + mRectWidth + mGapDistance, mStartY + mRectWidth + mGapDistance, mPaint)
    }

    private fun drawCenterLine(canvas: Canvas?) {
        canvas?.drawRect(mStartX, mLineStartY, mStartX + mLineWidth, mLineStartY + centerLineHeight, mPaint)
        mLineStartY += LINE_STEP
        if (mLineStartY > (mStartY + mRectWidth)) {
            mLineStartY = mStartY
        }
    }

    private fun drawPossiblePoints(canvas: Canvas?) {
        if (mCameraManager == null || mPointList.isEmpty()) return
        // LogUtil.e("Start draw points. points size ${mPointList.size}")
        val framingRect = mCameraManager?.getFramingRect() ?: return
        val previewRect = mCameraManager?.getFramingRectInPreview() ?: return
        val scaleX = mRectWidth * 1.0f / previewRect.width()
        val scaleY = mRectWidth * 1.0f / previewRect.height()

        var currentPoints = mPointList
        var lastPoints = mLastPointList
        val frameLeft = framingRect.left
        val frameTop = framingRect.top

        if (currentPoints.isEmpty()) {
            mLastPointList = null
        } else {
            mPointList = ArrayList(5)
            mLastPointList = currentPoints
            mPaint.alpha = POINT_ALPHA
            synchronized(currentPoints) {
                for (point in currentPoints) {
                    canvas?.drawCircle((frameLeft + (point.x * scaleX).toInt()).toFloat(),
                            (frameTop + (point.y * scaleY).toInt()).toFloat(),
                            mPointRadius, mPaint)
                }
            }
        }

        if (lastPoints != null) {
            mPaint.alpha = POINT_ALPHA / 2
            synchronized(lastPoints) {
                val radius = mPointRadius / 2
                for (point in lastPoints) {
                    canvas?.drawCircle((frameLeft + (point.x * scaleX).toInt()).toFloat(),
                            (frameTop + (point.y * scaleY).toInt()).toFloat(),
                            radius, mPaint)
                }
            }
        }
    }

    fun setCameraManager(cameraManager: CameraManager?) {
        this.mCameraManager = cameraManager
    }

    fun addPossibleResultPoint(point: ResultPoint?) {
        // LogUtil.i("Found point: [${point?.x}, ${point?.y}]")
        if (point == null) return
        val points = mPointList
        synchronized(points) {
            points.add(point)
            val size = points.size
            if (size > MAX_RESULT_POINTS) {
                points.subList(0, mPointList.size - MAX_RESULT_POINTS / 2).clear()
            }
        }
    }

    companion object {
        const val LINE_STEP: Int = 10
        const val ANIMATE_DELAY: Long = 80L
        const val MAX_RESULT_POINTS: Int = 20
        const val POINT_ALPHA = 0xA0
    }
}