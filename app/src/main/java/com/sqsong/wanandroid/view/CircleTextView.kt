package com.sqsong.wanandroid.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View

import com.sqsong.wanandroid.R

/**
 * Created by 青松 on 2017/6/22.
 */

class CircleTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var text: String? = null
    private var mPaint: Paint? = null
    private var textColor: Int = 0
    private var padding: Float = 0.toFloat()
    private var textSize: Float = 0.toFloat()
    private var borderColor: Int = 0
    private var borderWidth: Float = 0.toFloat()
    private var backgroundColor: Int = 0

    init {
        initAttrs(context, attrs)
        init()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView)
            backgroundColor = typedArray!!.getColor(R.styleable.CircleTextView_backgroundColor, Color.CYAN)
            textColor = typedArray.getColor(R.styleable.CircleTextView_textColor, Color.WHITE)
            textSize = typedArray.getDimension(R.styleable.CircleTextView_textSize, resources.getDimension(R.dimen.default_text_size))
            padding = typedArray.getDimension(R.styleable.CircleTextView_padding, resources.getDimension(R.dimen.default_padding))
            borderWidth = typedArray.getDimension(R.styleable.CircleTextView_borderWidth, 0f)
            borderColor = typedArray.getColor(R.styleable.CircleTextView_borderColor, Color.RED)
            text = typedArray.getString(R.styleable.CircleTextView_text)

            if (text == null) {
                text = ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray?.recycle()
        }
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.isDither = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mPaint!!.textSize = textSize
        val textWidth = mPaint!!.measureText(text).toInt()
        val fontMetrics = mPaint!!.fontMetrics
        val textHeight = (fontMetrics.bottom - fontMetrics.top).toInt()
        val size = (Math.max(textWidth, textHeight).toFloat() + borderWidth * 2 + padding * 2).toInt()
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
        drawBackground(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        mPaint!!.color = textColor
        mPaint!!.textSize = textSize
        val x = width / 2 - mPaint!!.measureText(text) / 2
        val baseY = height / 2 - (mPaint!!.descent() + mPaint!!.ascent()) / 2
        canvas.drawText(text!!, x, baseY, mPaint!!)
    }

    private fun drawBackground(canvas: Canvas) {
        mPaint!!.color = backgroundColor
        mPaint!!.style = Paint.Style.FILL
        val radius = width / 2 - borderWidth
        canvas.drawCircle((height / 2).toFloat(), (height / 2).toFloat(), radius, mPaint!!)
    }

    private fun drawBorder(canvas: Canvas) {
        if (borderWidth <= 0) return
        mPaint!!.color = borderColor
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = borderWidth
        val radius = (width - borderWidth) / 2
        canvas.drawCircle((height / 2).toFloat(), (height / 2).toFloat(), radius, mPaint!!)
    }

    fun setText(text: String) {
        if (!TextUtils.isEmpty(text)) {
            this.text = text
            invalidate()
        }
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
    }
}
