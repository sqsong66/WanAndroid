package com.sqsong.wanandroid.view

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.CommonUtil

class DefaultPageLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    private var mContentLayout: View? = null
    private var mLoadingLayout: View? = null
    private var mEmptyLayout: View? = null
    private var mErrorLayout: View? = null

    private fun showLayout(@DefaultPageType type: Int) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            changeLayoutVisibility(type)
        } else {
            post { changeLayoutVisibility(type) }
        }
    }

    private fun changeLayoutVisibility(@DefaultPageType type: Int) {
        mContentLayout?.visibility = if (type == TYPE_CONTENT) View.VISIBLE else View.GONE
        mLoadingLayout?.visibility = if (type == TYPE_LOADING) View.VISIBLE else View.GONE
        mEmptyLayout?.visibility = if (type == TYPE_EMPTY) View.VISIBLE else View.GONE
        mErrorLayout?.visibility = if (type == TYPE_ERROR) View.VISIBLE else View.GONE
    }

    fun showContentLayout() {
        showLayout(TYPE_CONTENT)
    }

    fun showLoadingLayout() {
        showLayout(TYPE_LOADING)
    }

    fun showEmptyLayout() {
        showLayout(TYPE_EMPTY)
    }

    fun showErrorLayout() {
        showLayout(TYPE_ERROR)
    }

    class Builder(private val context: Context) {

        private val inflater = LayoutInflater.from(context)

        private var targetPage: Any? = null
        private var pageLayout: DefaultPageLayout? = null

        @LayoutRes
        private var loadingResId: Int? = null

        @LayoutRes
        private var emptyResId: Int? = null

        @IdRes
        private var emptyDescTvId: Int? = null

        private var emptyDesc: String? = null

        @LayoutRes
        private var errorResId: Int? = null

        @IdRes
        private var errorRetryBtnId: Int? = null

        @IdRes
        private var errorDescTvId: Int? = null

        private var errorDesc: String? = null

        private var retryListener: OnRetryClickListener? = null

        /**
         * 设置需要填充默认加载页、空白页、错误页的目标， 该目标可以是[Activity]、[Fragment]、[View]
         */
        fun setTargetPage(target: Any): Builder {
            this.targetPage = target
            return this@Builder
        }

        /**
         * @param layoutId 页面加载布局
         * @return [Builder]本身
         */
        fun setLoadingLayout(@LayoutRes layoutId: Int): Builder {
            this.loadingResId = layoutId
            return this@Builder
        }

        /**
         * @param layoutId 空白页面布局
         * @param descId 空白页面描述的[TextView]id
         * @param desc 空白页面的描述
         * @return [Builder]本身
         */
        fun setEmptyLayout(@LayoutRes layoutId: Int, @IdRes descId: Int? = null, desc: String? = null): Builder {
            this.emptyResId = layoutId
            this.emptyDescTvId = descId
            this.emptyDesc = desc
            return this@Builder
        }

        /**
         * @param layoutId 错误页面的布局
         * @param retryId 重试按钮[Button]的布局id
         * @param errorDescId 错误描述[TextView]的布局id
         * @param errorDesc 错误描述
         * @return [Builder]本身
         */
        fun setErrorLayout(@LayoutRes layoutId: Int, @IdRes retryId: Int? = null,
                           @IdRes errorDescId: Int? = null, errorDesc: String? = null,
                           retryListener: OnRetryClickListener? = null): Builder {
            this.errorResId = layoutId
            this.errorRetryBtnId = retryId
            this.errorDescTvId = errorDescId
            this.errorDesc = errorDesc
            this.retryListener = retryListener
            return this@Builder
        }

        fun setOnRetryClickListener(listener: OnRetryClickListener): Builder {
            this.retryListener = listener
            return this@Builder
        }

        fun build(): DefaultPageLayout {
            pageLayout = buildPageLayout()
            buildLayoutPage()
            // pageLayout?.showContentLayout()
            return pageLayout!!
        }

        private fun buildLayoutPage() {
            inflateLoadingLayout(loadingResId ?: R.layout.layout_default_loading, R.id.progress)
            if (emptyResId != null) {
                inflateEmptyLayout(emptyResId!!, emptyDescTvId, emptyDesc!!)
            } else {
                inflateEmptyLayout(R.layout.layout_default_empty, R.id.error_desc_tv, context.getString(R.string.text_empty_tips))
            }
            if (errorResId != null) {
                inflateErrorLayout(errorResId!!, errorRetryBtnId, errorDescTvId, errorDesc!!)
            } else {
                inflateErrorLayout(R.layout.layout_default_error, R.id.retry_btn, R.id.error_desc_tv, context.getString(R.string.text_error_tips))
            }
        }

        private fun inflateLoadingLayout(@LayoutRes resId: Int, @IdRes progressbarId: Int?) {
            inflater.inflate(resId, pageLayout, false).apply {
                pageLayout?.mLoadingLayout = this
                pageLayout?.addView(this)
                if (progressbarId != null) {
                    val progressBar = findViewById<ProgressBar>(progressbarId)
                    CommonUtil.setProgressbarColor(progressBar)
                }
            }
        }

        private fun inflateEmptyLayout(@LayoutRes resId: Int, @IdRes emptyId: Int?, emptyDesc: String) {
            inflater.inflate(resId, pageLayout, false).apply {
                pageLayout?.mEmptyLayout = this
                pageLayout?.addView(this)
                if (emptyId != null) {
                    CommonUtil.setProgressbarColor(findViewById(R.id.progress))
                    val descTv = findViewById<TextView>(emptyId)
                    descTv.text = emptyDesc
                }
            }
        }

        private fun inflateErrorLayout(@LayoutRes resId: Int, @IdRes retryId: Int?,
                                       @IdRes errorDescId: Int?, errorDesc: String) {
            inflater.inflate(resId, pageLayout, false).apply {
                pageLayout?.mErrorLayout = this
                pageLayout?.addView(this)
                if (retryId != null) {
                    val retryBtn = findViewById<Button>(retryId)
                    retryBtn.setOnClickListener {
                        retryListener?.onRetry()
                    }
                    if (errorDescId != null) {
                        val descTv = findViewById<TextView>(errorDescId)
                        descTv.text = errorDesc
                    }
                }
            }
        }

        private fun buildPageLayout(): DefaultPageLayout {
            val pageLayout = DefaultPageLayout(context)
            val container: ViewGroup = getContainerView()
            val contentView: View
            var viewIndex = 0
            if (targetPage is View) {
                val count = container.childCount
                for (i in 0 until count) {
                    if (container.getChildAt(i) == targetPage) {
                        viewIndex = i
                        break
                    }
                }
                contentView = targetPage as View
            } else {
                contentView = container.getChildAt(0)
            }
            pageLayout.mContentLayout = contentView
            // 获取到布局View的布局参数，然后让其父布局移除它自己，然后将我们的layout添加到父布局中去
            val params = contentView.layoutParams
            container.removeView(contentView)
            // 先将容器清空， 然后添加布局中的View
            pageLayout.removeAllViews()
            pageLayout.addView(contentView)
            container.addView(pageLayout, viewIndex, params)
            return pageLayout
        }

        private fun getContainerView(): ViewGroup {
            if (targetPage == null) throw IllegalArgumentException("The target page cannot be null.")
            return when (targetPage) {
                is Activity -> (targetPage as Activity).findViewById(R.id.content)
                is Fragment -> (targetPage as Fragment).view?.parent as ViewGroup
                is View -> (targetPage as View).parent as ViewGroup
                else -> throw IllegalArgumentException("Not support this page layout: $targetPage")
            }
        }

    }

    companion object {
        const val TYPE_CONTENT: Int = 0
        const val TYPE_LOADING: Int = 1
        const val TYPE_EMPTY: Int = 2
        const val TYPE_ERROR: Int = 3
    }

    interface OnRetryClickListener {
        fun onRetry()
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_CONTENT, TYPE_LOADING, TYPE_EMPTY, TYPE_ERROR)
    annotation class DefaultPageType

}