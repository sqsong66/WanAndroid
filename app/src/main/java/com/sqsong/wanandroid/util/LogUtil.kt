package com.sqsong.wanandroid.util

import android.util.Log
import com.sqsong.wanandroid.BuildConfig

/**
 * Created by Administrator on 2017/9/28.
 */

object LogUtil {

    var DEBUG = BuildConfig.DEBUG

    fun v(tag: String, msg: String) {
        if (DEBUG) {
            Log.v(tag, msg)
        }
    }

    fun v(msg: String) {
        if (DEBUG) {
            Log.v(getClassName(2), msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun d(msg: String) {
        if (DEBUG) {
            Log.d(getClassName(2), msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun i(msg: String) {
        if (DEBUG) {
            Log.i(getClassName(2), msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (DEBUG) {
            Log.w(tag, msg)
        }
    }

    fun w(msg: String) {
        if (DEBUG) {
            Log.w(getClassName(2), msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun e(msg: String) {
        if (DEBUG) {
            Log.e(getClassName(2), msg)
        }
    }

    private fun getClassName(frame: Int): String {
        val frames = Throwable().stackTrace
        return parseClassName(frames[frame].className)
    }

    private fun parseClassName(fullName: String): String {
        val lastDot = fullName.lastIndexOf('.')
        var simpleName = fullName
        if (lastDot != -1) {
            simpleName = fullName.substring(lastDot + 1)
        }
        // handle inner class names
        val lastDollar = simpleName.lastIndexOf('$')
        if (lastDollar != -1) {
            simpleName = simpleName.substring(0, lastDollar)
        }
        return simpleName
    }

}
