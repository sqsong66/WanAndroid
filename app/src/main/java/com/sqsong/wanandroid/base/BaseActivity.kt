package com.sqsong.wanandroid.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sqsong.wanandroid.theme.ThemeOverlayUtil

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Get the content layout resource id.
     * @return layout resource id.
     */
    abstract fun getLayoutResId(): Int

    abstract fun initEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeOverlayUtil.applyThemeOverlay(this)
        super.onCreate(savedInstanceState)
        beforeInflateView()
        setContentView(getLayoutResId())
        initView()
        initEvent()
        BaseApplication.INSTANCE.addActivity(this)
    }

    /**
     * find some views, in kotlin maybe useless.
     */
    open fun initView() {

    }

    /**
     * Before inflate the content layout resource. In this function,
     * you can do nothing or override it do something, like get intent data, etc.
     */
    open fun beforeInflateView() {

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
        BaseApplication.INSTANCE.removeActivity(this)
    }
}