package com.sqsong.wanandroid.ui.splash

import android.content.Intent
import android.widget.TextView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.common.inter.TranslucentNavigation
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.splash.mvp.SplashContract
import com.sqsong.wanandroid.ui.splash.mvp.SplashPresenter
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.ext.showImage
import kotlinx.android.synthetic.main.activity_splash.*

@TranslucentNavigation
class SplashActivity : BaseActivity<SplashPresenter>(), SplashContract.View, IAppCompatActivity {

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun initEvent() {
        versionTv.text = String.format(getString(R.string.text_version_info), CommonUtil.getVersionName(this))
        mPresenter.onAttach(this)
    }

    override fun getTimerTextView(): TextView {
        return timerTv
    }

    override fun getAndroidText(): TextView {
        return androidTv
    }

    override fun getPlayText(): TextView {
        return playTv
    }

    override fun showTime(time: String) {
        timerTv.text = time
    }

    override fun showBackgroundImage(resId: Int) {
        splashIv.showImage(this, resId, -1)
    }

    override fun startNewActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
