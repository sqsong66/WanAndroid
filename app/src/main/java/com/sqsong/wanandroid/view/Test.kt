package com.sqsong.wanandroid.view


import android.os.Handler

class Test {
    private val mHandler = Handler()

    private val runnable = object : Runnable {
        override fun run() {
            mHandler.postDelayed(this, 100)
        }
    }

}
