package com.sqsong.wanandroid.home

import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseActivity
import com.sqsong.wanandroid.base.BaseApplication
import com.sqsong.wanandroid.theme.ColorPalette
import com.sqsong.wanandroid.theme.OnThemeOverlaySavedListener
import com.sqsong.wanandroid.theme.ThemeOverlayUtil
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.util.showCircleImage
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : BaseActivity(), OnThemeOverlaySavedListener {

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

    }

    override fun onThemeOverlaySaved(palette: ColorPalette?, themeIndex: Int) {
        ThemeOverlayUtil.setThemeOverlay(this, palette?.themeOverlay)
    }

}
