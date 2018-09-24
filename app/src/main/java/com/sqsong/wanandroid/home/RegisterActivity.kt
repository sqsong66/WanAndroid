package com.sqsong.wanandroid.home

import android.view.View
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseActivity
import com.sqsong.wanandroid.util.setupUi
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun initEvent() {
        setupUi(root)
        back_iv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_iv -> finish()
        }
    }
}
