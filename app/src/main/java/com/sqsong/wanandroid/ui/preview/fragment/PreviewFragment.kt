package com.sqsong.wanandroid.ui.preview.fragment

import android.os.Bundle
import android.text.TextUtils
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.GlideApp
import com.sqsong.wanandroid.ui.base.BaseFragment
import com.sqsong.wanandroid.ui.preview.mvp.PreviewContract
import com.sqsong.wanandroid.ui.preview.mvp.PreviewPresenter
import com.sqsong.wanandroid.util.Constants
import io.reactivex.Observable
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_preview.*

class PreviewFragment : BaseFragment(), PreviewContract.View {

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

    override fun getLayoutResId(): Int = R.layout.fragment_preview

    override fun initEvent() {
        mPresenter?.onAttach(this)
        imageTouch.displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
        imageTouch.setSingleTapListener { finishActivity() }

        GlideApp.with(this)
                .load(mImageUrl)
                .fitCenter()
                .placeholder(R.drawable.image_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageTouch)
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