package com.sqsong.wanandroid.home

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.util.setupUi
import kotlinx.android.synthetic.main.activity_register.*

@ChangeThemeAnnotation
class RegisterActivity : AppCompatActivity(), IAppCompatActivity, View.OnClickListener {

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
