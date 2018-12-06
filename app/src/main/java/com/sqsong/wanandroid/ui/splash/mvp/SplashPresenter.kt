package com.sqsong.wanandroid.ui.splash.mvp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.ui.home.activity.MainActivity
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.PreferenceHelper.get
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashPresenter @Inject constructor(private val splashView: SplashContract.View,
                                          private val disposable: CompositeDisposable) :
        BasePresenter<SplashContract.View, IModel>() {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mPreferences: SharedPreferences

    private val mImageList = listOf(R.drawable.splash_image01, R.drawable.splash_image02,
            R.drawable.splash_image03, R.drawable.splash_image04, R.drawable.splash_image05)

    override fun onAttach(view: SplashContract.View) {
        mView = splashView
        registerEvents()
    }

    private fun registerEvents() {
        setTextAnimation()
        setTimeTvParams()
        mView?.showBackgroundImage(mImageList[Random().nextInt(mImageList.size)])
        disposable.add(intervalDisposable())
        addDisposable(timerDisposable())
    }

    private fun addDisposable(dispos: Disposable?) {
        if (dispos != null) {
            disposable.add(dispos)
        }
    }

    private fun timerDisposable(): Disposable? {
        if (mView == null) return null
        return RxView.clicks(mView!!.getTimerTextView())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startNewActivity() }
    }

    private fun setTextAnimation() {
        val languageType = mPreferences[Constants.LANGUAGE_TYPE, 0]
        CommonUtil.setAssetsTextFont(mView?.getAndroidText(), languageType)
        CommonUtil.setAssetsTextFont(mView?.getPlayText(), languageType)

        val halfScreenWidth = DensityUtil.getScreenWidth() * 1.0f / 2
        val textPaint = mView?.getPlayText()?.paint
        val playTextWidth = textPaint?.measureText(mView?.getPlayText()?.text.toString()) ?: .0f
        val androidTextWidth = textPaint?.measureText(mView?.getAndroidText()?.text.toString())
                ?: .0f
        val playDistance = halfScreenWidth + playTextWidth / 2
        val androidDistance = halfScreenWidth + androidTextWidth / 2
        val playTranslationX = ObjectAnimator.ofFloat(mView?.getPlayText(), "translationX", 0f, playDistance)
        val androidTranslationX = ObjectAnimator.ofFloat(mView?.getAndroidText(), "translationX", 0f, -androidDistance)
        val playScaleX = ObjectAnimator.ofFloat(mView?.getPlayText(), "scaleX", .5f, 1.0f)
        val playScaleY = ObjectAnimator.ofFloat(mView?.getPlayText(), "scaleY", .5f, 1.0f)
        val androidScaleX = ObjectAnimator.ofFloat(mView?.getAndroidText(), "scaleX", .5f, 1.0f)
        val androidSlayScaleY = ObjectAnimator.ofFloat(mView?.getAndroidText(), "scaleY", .5f, 1.0f)
        val set = AnimatorSet()
        set.playTogether(playTranslationX, androidTranslationX, playScaleX, playScaleY, androidScaleX, androidSlayScaleY)
        set.duration = 1200
        set.interpolator = OvershootInterpolator()
        mView?.getAndroidText()?.postDelayed({ set.start() }, 500)
    }

    private fun intervalDisposable(): Disposable {
        return Observable.intervalRange(0, Constants.SPLASH_TIME, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    mView?.showTime(String.format(mContext.getString(R.string.text_second), Constants.SPLASH_TIME - it))
                }
                .doOnComplete {
                    startNewActivity()
                }
                .subscribe()
    }


    private fun setTimeTvParams() {
        val layoutParams = mView?.getTimerTextView()?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topMargin = DensityUtil.getStatusBarHeight(mContext) + DensityUtil.dip2px(24)
    }

    private fun startNewActivity() {
        disposable.clear()
        mView?.startNewActivity(MainActivity::class.java)
    }
}