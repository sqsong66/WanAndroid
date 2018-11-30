package com.sqsong.wanandroid.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.RingtoneManager
import android.net.Uri
import android.text.TextUtils
import android.util.TypedValue
import android.widget.ProgressBar
import android.widget.TextView
import com.sqsong.wanandroid.R
import java.text.SimpleDateFormat
import java.util.*

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

    fun parseUrlParameter(url: String?, paramName: String): String? {
        if (TextUtils.isEmpty(url)) return null
        val uri = Uri.parse(url)
        return uri.getQueryParameter(paramName)
    }

    fun buildShareIntent(title: String, link: String): Intent {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/html"
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, link)
        sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE, title)
        return sharingIntent
    }

    fun buildBrowserIntent(link: String?): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(link))
    }

    fun convertListToString(list: MutableList<String>?): String {
        if (list == null || list.isEmpty()) return ""
        if (list.size > 5) {
            var subList = list.subList(0, 5)
            list.clear()
            list.addAll(subList)
        }
        val buffer = StringBuffer()
        for (i in 0 until list.size) {
            buffer.append(list[i])
            if (i != list.size - 1) {
                buffer.append(",")
            }
        }
        return buffer.toString()
    }

    fun formatLongTimeToString(timestamp: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.US)
        try {
            return sdf.format(Date(timestamp))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun playDefaultSound(context: Context) {
        try {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone.play()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}