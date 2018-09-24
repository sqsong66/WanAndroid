package com.sqsong.wanandroid.home

import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseActivity
import com.sqsong.wanandroid.util.setupUi
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initEvent() {
        setupUi(root)
        close_iv.setOnClickListener(this)
        login_btn.setOnClickListener(this)
        register_tips_tv.setOnClickListener(this)
        forget_password_tv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.close_iv -> finish()
            R.id.login_btn -> startActivity(Intent(this, HomeActivity::class.java))
            R.id.register_tips_tv -> startActivity(Intent(this, RegisterActivity::class.java))
            R.id.forget_password_tv -> Snackbar.make(v, R.string.text_developing, Snackbar.LENGTH_SHORT).show()
        }
    }
}
