package com.sqsong.wanandroid.ui.web

import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.web.mvp.WebViewContract
import com.sqsong.wanandroid.ui.web.mvp.WebViewPresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.ext.setupSwipeLayoutColor
import com.sqsong.wanandroid.util.ext.setupToolbar
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.content_webview.*

class WebViewActivity : BaseActivity<WebViewPresenter>(), WebViewContract.View, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mLinkUrl: String
    private lateinit var mWebTitle: String
    private lateinit var onScrollListener: ViewTreeObserver.OnScrollChangedListener

    override fun beforeInflateView() {
        mWebTitle = intent.getStringExtra(Constants.KEY_WEB_TITLE)
        mLinkUrl = intent.getStringExtra(Constants.KEY_WEB_URL)
    }

    override fun getLayoutResId(): Int = R.layout.activity_webview

    override fun initEvent() {
        setupToolbar(toolbar)
        setupSwipeLayoutColor(swipeLayout)
        swipeLayout.setOnRefreshListener(this)
        mPresenter.onAttach(this)
        toolbar.post {
            toolbar.title = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(mWebTitle, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(mWebTitle)
            }
        }

        onScrollListener = ViewTreeObserver.OnScrollChangedListener {
            swipeLayout.isEnabled = webView.scrollY == 0
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.action_browser -> mPresenter.openBrowser(mLinkUrl)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        mPresenter.refreshWeb()
    }

    override fun getInitUrl(): String = mLinkUrl

    override fun getToolbar(): Toolbar = toolbar

    override fun getWebView(): WebView = webView

    override fun getProgressBar(): ProgressBar = progressBar

    override fun startNewActivity(buildBrowserIntent: Intent) = startActivity(buildBrowserIntent)

    override fun showRefreshBar() {
        swipeLayout.isRefreshing = true
    }

    override fun hideRefreshBar() {
        swipeLayout.isRefreshing = false
    }

    override fun onStart() {
        super.onStart()
        swipeLayout.viewTreeObserver.addOnScrollChangedListener(onScrollListener)
    }

    override fun onStop() {
        super.onStop()
        swipeLayout.viewTreeObserver.removeOnScrollChangedListener(onScrollListener)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}