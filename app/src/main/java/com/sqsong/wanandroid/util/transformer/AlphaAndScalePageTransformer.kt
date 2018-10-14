package com.sqsong.wanandroid.util.transformer

import android.view.View

import androidx.viewpager.widget.ViewPager

/*******************************************************************
 * * * * *   * * * *   *     *       Created by OCN.Yang
 * *     *   *         * *   *       Time:2017/12/8 11:28.
 * *     *   *         *   * *       Email address:ocnyang@gmail.com
 * * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 */


class AlphaAndScalePageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val scale = if (position < 0)
            (1 - SCALE_MAX) * position + 1
        else
            (SCALE_MAX - 1) * position + 1
        val alpha = if (position < 0)
            (1 - ALPHA_MAX) * position + 1
        else
            (ALPHA_MAX - 1) * position + 1
        if (position < 0) {
            page.pivotX = page.width.toFloat()
            page.pivotY = (page.height / 2).toFloat()
        } else {
            page.pivotX = 0f
            page.pivotY = (page.height / 2).toFloat()
        }
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = Math.abs(alpha)
    }

    companion object {
        const val SCALE_MAX = 0.8f
        const val ALPHA_MAX = 0.6f
    }
}
