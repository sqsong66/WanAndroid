package com.sqsong.wanandroid.util

import android.content.Context
import android.graphics.Color
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
        val a = progressBar?.context?.obtainStyledAttributes(TypedValue().data, intArrayOf(R.attr.colorProgress))
        val color = a?.getColor(0, 0)
        a?.recycle()
        drawable?.setColorFilter(color!!, android.graphics.PorterDuff.Mode.SRC_IN)
        progressBar?.progressDrawable = drawable
    }

    fun getThemeColor(context: Context, attrColor: Int): Int {
        val typedArray = context.obtainStyledAttributes(TypedValue().data, intArrayOf(attrColor))
        val color = typedArray.getColor(0, Color.BLACK)
        typedArray.recycle()
        return color
    }

    fun setAssetsTextFont(textView: TextView, assetsFont: String) {
        try {
            textView.typeface = Typeface.createFromAsset(textView.context.assets, assetsFont)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}