package com.sqsong.wanandroid.home

import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.showCircleImage
import kotlinx.android.synthetic.main.content_home.*

@ChangeThemeAnnotation
class HomeActivity : AppCompatActivity(), IAppCompatActivity {

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
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
