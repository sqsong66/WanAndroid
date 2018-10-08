package com.sqsong.wanandroid.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager : ViewPager {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return false/*super.onTouchEvent(ev)*/
    }

    override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
        return false/*super.onInterceptHoverEvent(event)*/
    }
}
