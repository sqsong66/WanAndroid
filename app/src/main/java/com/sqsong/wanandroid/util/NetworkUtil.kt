package com.sqsong.wanandroid.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    fun isNetworkAvailable(context: Context): Boolean {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conn.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}