package com.sqsong.wanandroid.util

import android.text.TextUtils

object CommonUtil {

    fun isPhoneNumberValid(phone: String): Boolean {
        if (TextUtils.isEmpty(phone) || phone.length != 11) return false
        return true
    }

}