package com.sqsong.wanandroid.util.transformer

import android.view.View
import androidx.viewpager.widget.ViewPager

class ZoomPageTransformer(private val mViewPager: ViewPager) : ViewPager.PageTransformer {

    private var mOffset: Float = 0.toFloat()
    private var mTranslationX: Float = 0.toFloat()

    override fun transformPage(view: View, position: Float) {
        if (mOffset == 0f) {
            val paddingLeft = mViewPager.paddingLeft.toFloat()
            val paddingRight = mViewPager.paddingRight.toFloat()
            val width = mViewPager.width.toFloat()
            mOffset = paddingLeft / (width - paddingLeft - paddingRight)
        }
        val currentPos = position - mOffset
        view.apply {
            if (mTranslationX == 0f) {
                mTranslationX = (2.0f - MAX_SCALE_FACTOR - MIN_SCALE_FACTOR) * width / 2.0f
            }
            when {
                currentPos <= -1.0f -> {
                    translationX = mTranslationX
                    scaleX = MIN_SCALE_FACTOR
                    scaleY = MIN_SCALE_FACTOR
                }
                currentPos <= 1.0f -> {
                    val scale = (MAX_SCALE_FACTOR - MIN_SCALE_FACTOR) * Math.abs(1.0f - Math.abs(currentPos))
                    val transX = -mTranslationX * currentPos
                    translationX = transX
                    scaleX = MIN_SCALE_FACTOR + scale
                    scaleY = MIN_SCALE_FACTOR + scale
                }
                else -> {
                    translationX = -mTranslationX
                    scaleX = MIN_SCALE_FACTOR
                    scaleY = MIN_SCALE_FACTOR
                }
            }
        }
    }

    companion object {
        const val MAX_SCALE_FACTOR = 1.0f
        const val MIN_SCALE_FACTOR = 0.85f
    }
}
