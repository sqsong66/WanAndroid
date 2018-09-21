package com.sqsong.wanandroid.util

import android.view.View

fun View.dpToPx(dp: Int): Int {
    var scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}