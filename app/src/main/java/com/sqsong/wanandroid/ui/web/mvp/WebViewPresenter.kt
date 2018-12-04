package com.sqsong.wanandroid.ui.web.mvp

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.util.CommonUtil
import javax.inject.Inject

class WebViewPresenter @Inject constructor() : BasePresenter<WebViewContract.View, IModel>() {

    private var mCurrentUrl: String? = null

    override fun onAttach(view: WebViewContract.View) {
        super.onAttach(view)
        setupParams()
    }

    private fun setupParams() {
        mCurrentUrl = mView?.getInitUrl()
        mView?.showRefreshBar()
        setWebSettings()
        setupWebClient()
    }

    private fun setWebSettings() {
        val webSettings = mView?.getWebView()?.settings
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings?.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        webSettings?.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings?.loadWithOverviewMode = true // 缩放至屏幕的大小
        //缩放操作
        webSettings?.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings?.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings?.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        webSettings?.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings?.allowFileAccess = true //设置可以访问文件
        webSettings?.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings?.loadsImagesAutomatically = true //支持自动加载图片
        webSettings?.defaultTextEncodingName = "UTF-8"//设置编码格式
    }

    private fun setupWebClient() {
        mView?.getWebView()?.webViewClient = NewWebViewClient()
        mView?.getWebView()?.webChromeClient = ProgressWebViewChromeClient()

        refreshWeb()
    }

    fun refreshWeb() {
        mView?.getWebView()?.loadUrl(mCurrentUrl)
    }

    override fun onDestroy() {
        if ((mView?.getWebView())?.parent != null) {
            (mView?.getWebView()?.parent as ViewGroup).removeView(mView?.getWebView())
        }
        mView?.getWebView()?.removeAllViews()
        mView?.getWebView()?.destroy()
        super.onDestroy()
    }

    fun openBrowser(linkUrl: String) {
        mView?.startNewActivity(CommonUtil.buildBrowserIntent(linkUrl))
    }

    inner class ProgressWebViewChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            mView?.getProgressBar()?.progress = newProgress
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            if (TextUtils.isEmpty(title)) {
                mView?.getToolbar()?.title = title
            }
        }
    }

    inner class NewWebViewClient : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            mCurrentUrl = request?.url.toString()
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            mCurrentUrl = url
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            mView?.getProgressBar()?.visibility = View.VISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            mView?.getProgressBar()?.visibility = View.GONE
            mView?.hideRefreshBar()
            super.onPageFinished(view, url)
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            handler?.proceed()
        }
    }

}