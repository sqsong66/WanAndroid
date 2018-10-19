package com.sqsong.wanandroid.util

import android.graphics.Typeface
import android.text.TextUtils
import android.util.TypedValue
import android.widget.ProgressBar
import android.widget.TextView
import com.sqsong.wanandroid.R

object CommonUtil {

    fun isPhoneNumberValid(phone: String): Boolean {
        if (TextUtils.isEmpty(phone) || phone.length != 11) return false
        return true
    }

    fun setProgressbarColor(progressBar: ProgressBar?) {
        val drawable = progressBar?.indeterminateDrawable?.mutate()
        val a = progressBar?.context?.obtainStyledAttributes(TypedValue().data, intArrayOf(R.attr.colorPrimary))
        val color = a?.getColor(0, 0)
        drawable?.setColorFilter(color!!, android.graphics.PorterDuff.Mode.SRC_IN)
        progressBar?.progressDrawable = drawable
    }

    fun setAssetsTextFont(textView: TextView, assetsFont: String) {
        try {
            textView.typeface = Typeface.createFromAsset(textView.context.assets, assetsFont)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}