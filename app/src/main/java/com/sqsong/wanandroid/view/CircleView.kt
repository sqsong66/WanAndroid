package com.sqsong.wanandroid.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.sqsong.wanandroid.R


class CircleView : View {

    private var mBgColor: Int? = 0
    private lateinit var mPaint: Paint

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context, attributeSet, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet, defStyleAttr)
    }

    private fun init(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleView, defStyleAttr, 0)
        mBgColor = typedArray?.getColor(R.styleable.CircleView_bgColor, ContextCompat.getColor(context, R.color.colorPrimary))
        typedArray.recycle()

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val w = width
        val h = height

        val pl = paddingLeft
        val pr = paddingRight
        val pt = paddingTop
        val pb = paddingBottom

        val usableWidth = w - (pl + pr)
        val usableHeight = h - (pt + pb)

        val radius = Math.min(usableWidth, usableHeight) / 2
        val cx = pl + usableWidth / 2
        val cy = pt + usableHeight / 2
        mPaint.color = mBgColor ?: ContextCompat.getColor(context, R.color.colorPrimary)
        canvas?.drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), mPaint)
    }

    open fun setBgColor(@ColorInt color: Int?) {
        mBgColor = color
        invalidate()
    }
}