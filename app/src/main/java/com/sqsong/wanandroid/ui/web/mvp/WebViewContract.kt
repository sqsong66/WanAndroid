package com.sqsong.wanandroid.ui.web.mvp

import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.sqsong.wanandroid.mvp.IView

interface WebViewContract {

    interface View : IView {
        fun getToolbar(): Toolbar
        fun getWebView(): WebView?
        fun getProgressBar(): ProgressBar
        fun getInitUrl(): String
        fun hideRefreshBar()
        fun showRefreshBar()
    }

}