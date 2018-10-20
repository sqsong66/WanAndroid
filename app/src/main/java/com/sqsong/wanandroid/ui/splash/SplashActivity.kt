package com.sqsong.wanandroid.ui.splash

import android.content.Intent
import android.widget.TextView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.splash.mvp.SplashContract
import com.sqsong.wanandroid.ui.splash.mvp.SplashPresenter
import com.sqsong.wanandroid.util.showImage
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity<SplashPresenter>(), SplashContract.View, IAppCompatActivity {

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun initEvent() {
        mPresenter.onAttach(this)
    }

    override fun getTimerTextView(): TextView {
        return timerTv
    }

    override fun showBackgroundImage(resId: Int) {
        splashIv.showImage(this, resId, -1)
    }

    override fun startNewActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
