package com.sqsong.wanandroid.ui.splash.mvp

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.ui.home.MainActivity
import com.sqsong.wanandroid.ui.login.LoginActivity
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
        BasePresenter<SplashContract.View, IModel>(null, disposable) {

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
        setTimeTvParams()
        mView.showBackgroundImage(mImageList[Random().nextInt(mImageList.size)])
        disposable.add(intervalDisposable())
        disposable.add(timerDisposable())
    }

    private fun intervalDisposable(): Disposable {
        return Observable.intervalRange(0, Constants.SPLASH_TIME, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    mView.showTime(String.format(mContext.getString(R.string.text_second), Constants.SPLASH_TIME - it))
                }
                .doOnComplete {
                    startNewActivity()
                }
                .subscribe()
    }


    private fun setTimeTvParams() {
        val layoutParams = mView.getTimerTextView().layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topMargin = DensityUtil.getStatusBarHeight(mContext) + DensityUtil.dip2px(24)
    }

    private fun timerDisposable(): Disposable {
        return RxView.clicks(mView.getTimerTextView())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { startNewActivity() }
    }

    private fun startNewActivity() {
        disposable.clear()
        val userName: String = mPreferences[Constants.LOGIN_USER_NAME] ?: ""
        val clazz: Class<*>
        clazz = if (TextUtils.isEmpty(userName)) {
            LoginActivity::class.java
        } else {
            MainActivity::class.java
        }
        mView.startNewActivity(clazz)
    }
}