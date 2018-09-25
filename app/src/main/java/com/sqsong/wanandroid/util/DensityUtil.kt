package com.sqsong.wanandroid.util

import android.content.Context
import android.content.res.Resources

class DensityUtil {

    companion object {
        @JvmStatic
        fun getScreenWidth(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        @JvmStatic
        fun getScreenHeight(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }

        @JvmStatic
        fun dip2px(dpValue: Int): Int {
            var scale = Resources.getSystem().displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        @JvmStatic
        fun getScreenDpWidth(context: Context): Float {
            val displayMetrics = context.resources.displayMetrics
            return displayMetrics.widthPixels / displayMetrics.density
        }

        @JvmStatic
        fun getScreenDpHeight(context: Context): Float {
            val displayMetrics = context.resources.displayMetrics
            return displayMetrics.heightPixels / displayMetrics.density
        }
    }

}