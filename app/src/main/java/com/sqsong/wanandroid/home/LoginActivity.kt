package com.sqsong.wanandroid.home

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseActivity
import com.sqsong.wanandroid.util.setupActionBar

class LoginActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initEvent() {
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }


}
