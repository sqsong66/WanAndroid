package com.sqsong.wanandroid.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.sqsong.wanandroid.R


object SnackbarUtil {

    fun showNormalSnack(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
    }

    fun showNormalToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showSnackText(view: View, text: String) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        /*val a = view.context.obtainStyledAttributes(TypedValue().data, intArrayOf(R.attr.colorPrimary))
        val color = a.getColor(0, 0)
        snackbar.view.setBackgroundColor(color)*/
        snackbar.view.setBackgroundResource(R.drawable.bg_snackbar)
        snackbar.show()
    }

    fun showToastText(context: Context, text: String) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_taost, null)
        val textView: TextView = view.findViewById<View>(R.id.text) as TextView
        textView.text = text
        val toast = Toast(context)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}