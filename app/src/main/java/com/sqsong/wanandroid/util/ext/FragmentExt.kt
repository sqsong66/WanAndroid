package com.sqsong.wanandroid.util.ext

import android.annotation.SuppressLint
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.R

@SuppressLint("ResourceType")
fun Fragment.setupSwipeLayoutColor(swipeRefreshLayout: SwipeRefreshLayout) {
    val ta = context?.obtainStyledAttributes(TypedValue().data,
            intArrayOf(R.attr.colorPrimaryLight, R.attr.colorPrimary, R.attr.colorPrimaryDark))
    val lightColor = ta?.getColor(0, ContextCompat.getColor(context!!, R.color.colorPrimaryLight))
    val primaryColor = ta?.getColor(1, ContextCompat.getColor(context!!, R.color.colorPrimary))
    val primaryDarkColor = ta?.getColor(2, ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
    swipeRefreshLayout.setColorSchemeColors(lightColor!!, primaryColor!!, primaryDarkColor!!)
    ta.recycle()
}