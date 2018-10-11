package com.sqsong.wanandroid.view

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class FixedSpeedScroller : Scroller {

    private var mDuration = 1000

    constructor(context: Context) : super(context)

    constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)

    constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(context, interpolator, flywheel) {}

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    fun setFixedDuration(duration: Int) {
        this.mDuration = duration
    }
}
