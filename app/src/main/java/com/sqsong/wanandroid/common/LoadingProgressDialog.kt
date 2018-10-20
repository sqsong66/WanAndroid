package com.sqsong.wanandroid.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.DensityUtil
import javax.inject.Inject

class LoadingProgressDialog @Inject constructor() : DialogFragment() {

    private lateinit var loadingTv: TextView

    private var mLoadingText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        mLoadingText = bundle?.getString(LOADING_TEXT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.dialog_loading_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingTv = view.findViewById(R.id.loading_tv)
        if (!TextUtils.isEmpty(mLoadingText)) {
            loadingTv.text = mLoadingText
        } else {
            loadingTv.text = context?.getString(R.string.text_loading)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = dialog.window.attributes
        params.width = DensityUtil.getScreenWidth() - DensityUtil.dip2px(30) * 2
    }

    companion object {

        private const val LOADING_TEXT: String = "loading_text"

        @JvmStatic
        fun newInstance(loadingText: String): LoadingProgressDialog {
            var dialog = LoadingProgressDialog()
            var bundle = Bundle()
            bundle.putString(LOADING_TEXT, loadingText)
            dialog.arguments = bundle
            return dialog
        }
    }
}