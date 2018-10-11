package com.sqsong.wanandroid.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.viewpager.widget.ViewPager
import com.sqsong.wanandroid.R
import java.util.*

/**
 * Created by 青松 on 2016/9/2.
 */
class CirclePagerIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mPaint: Paint? = null
    private var mDotSpace: Int = 0
    private var mCurrentPos: Int = 0
    private var mNormalColor: Int = 0
    private var mFocusedColor: Int = 0
    private var mDotCount = 3
    private var mIndicatorX: Float = 0.toFloat()
    private var mFocusRadius: Float = 0.toFloat()
    private var mNormalRadius: Float = 0.toFloat()
    private val mCirclePoints = ArrayList<PointF>()
    private val mStartInterpolator = LinearInterpolator()

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CirclePagerIndicator)
        mNormalColor = ta.getColor(R.styleable.CirclePagerIndicator_normalDotColor, Color.GRAY)
        mFocusedColor = ta.getColor(R.styleable.CirclePagerIndicator_focusedDotColor, Color.RED)
        mNormalRadius = ta.getDimension(R.styleable.CirclePagerIndicator_normalDotRadius, dip2px(3f).toFloat())
        mFocusRadius = ta.getDimension(R.styleable.CirclePagerIndicator_focusedDotRadius, dip2px(5f).toFloat())
        mDotSpace = ta.getDimensionPixelOffset(R.styleable.CirclePagerIndicator_dotSpace, dip2px(8f))
        ta.recycle()

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = (mDotCount.toFloat() * mFocusRadius * 2f + (mDotCount - 1) * mDotSpace).toInt()
        val measureHeight = (mFocusRadius * 2).toInt() + 5
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        prepareCirclePoints()
    }

    override fun onDraw(canvas: Canvas) {
        drawCircles(canvas)
        drawIndicator(canvas)
    }

    private fun drawCircles(canvas: Canvas) {
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = mNormalColor
        for (i in mCirclePoints.indices) {
            val pointF = mCirclePoints[i % mCirclePoints.size]
            canvas.drawCircle(pointF.x, pointF.y, mNormalRadius, mPaint!!)
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = mFocusedColor
        if (mCirclePoints.size > 0) {
            canvas.drawCircle(mIndicatorX, (height / 2).toFloat(), mFocusRadius, mPaint!!)
        }
    }

    private fun prepareCirclePoints() {
        mCirclePoints.clear()
        if (mDotCount > 0) {
            val y = height / 2
            val measureWidth = (mDotCount.toFloat() * mNormalRadius * 2f + (mDotCount - 1) * mDotSpace).toInt()
            val centerSpacing = (mNormalRadius * 2 + mDotSpace).toInt()
            var startX = ((width - measureWidth) / 2 + mNormalRadius).toInt()
            for (i in 0 until mDotCount) {
                val pointF = PointF(startX.toFloat(), y.toFloat())
                mCirclePoints.add(pointF)
                startX += centerSpacing
            }
            mIndicatorX = mCirclePoints[mCurrentPos % mCirclePoints.size].x
        }
    }

    private fun onPageScrolled(position: Int, positionOffset: Float) {
        if (mCirclePoints.isEmpty()) {
            return
        }

        val nextPosition = Math.min(mCirclePoints.size - 1, (position + 1) % mCirclePoints.size)
        val current = mCirclePoints[position % mCirclePoints.size]
        val next = mCirclePoints[nextPosition]

        mIndicatorX = current.x + (next.x - current.x) * mStartInterpolator.getInterpolation(positionOffset)
        invalidate()
    }

    private fun dip2px(dipValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun setViewPager(viewPager: ViewPager, dotCount: Int) {
        mDotCount = dotCount
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                this@CirclePagerIndicator.onPageScrolled(position, positionOffset)
            }

            override fun onPageSelected(position: Int) {
                mCurrentPos = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

}
