package com.sqsong.wanandroid.home

import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.HomeBannerBean
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.LogUtil
import com.sqsong.wanandroid.util.showCircleImage
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_home.*
import javax.inject.Inject

@ChangeThemeAnnotation
class HomeActivity : DaggerAppCompatActivity(), IAppCompatActivity {

    @Inject
    lateinit var mApiService: ApiService

    @Inject
    lateinit var disposable: CompositeDisposable

    override fun getLayoutResId(): Int {
        return R.layout.activity_home
    }

    override fun initEvent() {
        text.text = "Hello Kotlin."
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25.toFloat())
        text.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))

        image.showCircleImage(this, R.drawable.splash_image01)

        text.setOnClickListener {
            // LoadingProgressDialog.newInstance("").show(supportFragmentManager, "")
            ThemeSwitcherDialog().show(supportFragmentManager, "")
        }

        val screenDpWidth = DensityUtil.getScreenDpWidth(this)
        val screenDpHeight = DensityUtil.getScreenDpHeight(this)
        Log.e("sqsong", "Screen width dp: $screenDpWidth, height dp: $screenDpHeight")
        mApiService.getHomeBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ObserverImpl<HomeBannerBean>(disposable) {
                    override fun onFail(error: ApiException) {
                        LogUtil.e("sqsong", "Error code: " + error.code
                                + ", message: " + error.message + ", showMessage: " + error.showMessage)
                    }

                    override fun onSuccess(data: HomeBannerBean) {
                        LogUtil.e("sqsong", "Result -> " + data.toString())
                    }
                })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}
