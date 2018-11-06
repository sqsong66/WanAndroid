package com.sqsong.wanandroid.util.ext

import android.view.View

fun View.dpToPx(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}