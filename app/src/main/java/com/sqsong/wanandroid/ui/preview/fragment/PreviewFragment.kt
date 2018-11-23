package com.sqsong.wanandroid.ui.preview.fragment

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.preview.mvp.PreviewContract
import com.sqsong.wanandroid.ui.preview.mvp.PreviewPresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.ext.showImage
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_preview.*

class PreviewFragment : BaseFragment(), PreviewContract.View, ViewTreeObserver.OnPreDrawListener {


    private var mImageUrl: String? = null
    private var mPresenter: PreviewPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImageUrl = arguments?.getString(Constants.IMAGE_URL)
        if (TextUtils.isEmpty(mImageUrl)) {
            showMessage(getString(R.string.text_invalid_image_url))
            activity?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageTouch.viewTreeObserver.addOnPreDrawListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPreDraw(): Boolean {
        imageTouch.viewTreeObserver.removeOnPreDrawListener(this)
        activity?.startPostponedEnterTransition()
        return true
    }

    override fun getLayoutResId(): Int = R.layout.fragment_preview

    override fun initEvent() {
        mPresenter?.onAttach(this)
        imageTouch.showImage(context!!, mImageUrl!!)
        imageTouch.setSingleTapListener { finishActivity() }
    }

    fun setPresenter(presenter: PreviewPresenter) {
        this.mPresenter = presenter
    }

    override fun imageClickObservable(): Observable<Any> = RxView.clicks(imageTouch)

    override fun finishActivity() {
        activity?.finish()
    }

    companion object {
        fun newInstance(url: String): PreviewFragment {
            return PreviewFragment().apply {
                val bundle = Bundle()
                bundle.putString(Constants.IMAGE_URL, url)
                arguments = bundle
            }
        }
    }

}