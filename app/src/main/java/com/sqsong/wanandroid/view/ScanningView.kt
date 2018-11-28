package com.sqsong.wanandroid.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.DensityUtil

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
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawMusk(canvas)
        drawBorder(canvas)
        drawCenterLine(canvas)
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
        postInvalidateDelayed(ANIMATE_DELAY, mStartX.toInt(), mStartY.toInt(),
                (mStartX + mRectWidth).toInt(), (mStartY + mRectWidth).toInt())
    }

    companion object {
        const val LINE_STEP = 10
        const val ANIMATE_DELAY = 20L
    }
}