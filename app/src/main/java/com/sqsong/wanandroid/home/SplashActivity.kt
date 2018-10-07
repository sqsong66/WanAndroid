package com.sqsong.wanandroid.home

import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.login.LoginActivity
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.dpToPx
import com.sqsong.wanandroid.util.getStatusBarHeight
import com.sqsong.wanandroid.util.showImage
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity(), IAppCompatActivity, View.OnClickListener {

    private lateinit var timer: CountDownTimer

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun initEvent() {
        timer_tv.setOnClickListener(this)

        val params: ConstraintLayout.LayoutParams = timer_tv.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = getStatusBarHeight() + timer_tv.dpToPx(24)

        var images = listOf(R.drawable.splash_image01, R.drawable.splash_image02,
                R.drawable.splash_image03, R.drawable.splash_image04, R.drawable.splash_image05)
        splash_iv.showImage(this, images[Random().nextInt(5)], -1)

        timer = object : CountDownTimer(Constants.SPLASH_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.e("sqsong", "Time -> $millisUntilFinished")
                val time: Int = (millisUntilFinished / 1000).toInt()
                timer_tv.text = "$time s"
            }

            override fun onFinish() {
                startHomeActivity()
            }
        }
        timer.start()
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onClick(v: View?) {
        timer.cancel()
        startHomeActivity()
    }

}
