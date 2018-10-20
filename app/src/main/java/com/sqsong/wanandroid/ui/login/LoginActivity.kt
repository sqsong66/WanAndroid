package com.sqsong.wanandroid.ui.login

import android.content.Intent
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.LoadingProgressDialog
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.home.HomeActivity
import com.sqsong.wanandroid.ui.login.mvp.LoginContract
import com.sqsong.wanandroid.ui.login.mvp.LoginPresenter
import com.sqsong.wanandroid.util.registerClickEvent
import com.sqsong.wanandroid.util.registerTextChangeEvent
import com.sqsong.wanandroid.util.setupUi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@ChangeThemeAnnotation
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View, IAppCompatActivity {

    @Inject
    lateinit var mProgressDialog: LoadingProgressDialog

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initEvent() {
        setupUi(root)
        mPresenter.onAttach(this)
    }

    override fun getTitleText(): TextView {
        return loginTv
    }

    override fun closeDisposable(): Disposable {
        return registerClickEvent(closeIv) { finish() }
    }

    override fun userNameDisposable(): Disposable {
        return registerTextChangeEvent(userNameEdit, userNameInputLayout)
    }

    override fun passwordDisposable(): Disposable {
        return registerTextChangeEvent(passwordEdit, passwordInputLayout)
    }

    override fun commitObservable(): Observable<Any> {
        return RxView.clicks(loginBtn)
    }

    override fun userNameText(): String {
        return userNameEdit.text.toString().trim()
    }

    override fun passwordText(): String {
        return passwordEdit.text.toString().trim()
    }

    override fun showInputLayoutError(type: Int, errorMsg: String) {
        when (type) {
            0 -> {
                userNameInputLayout.isErrorEnabled = true
                userNameInputLayout.error = errorMsg
            }
            1, 2 -> {
                passwordInputLayout.isErrorEnabled = true
                passwordInputLayout.error = errorMsg
            }
        }
    }

    override fun showProcessDialog() {
        mProgressDialog.show(supportFragmentManager, "")
    }

    override fun hideProcessDialog() {
        mProgressDialog.dismiss()
    }

    override fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
