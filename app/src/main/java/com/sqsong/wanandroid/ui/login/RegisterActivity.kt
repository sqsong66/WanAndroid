package com.sqsong.wanandroid.ui.login

import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.LoadingProgressDialog
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.login.mvp.RegisterContract
import com.sqsong.wanandroid.ui.login.mvp.RegisterPresenter
import com.sqsong.wanandroid.util.ext.registerClickEvent
import com.sqsong.wanandroid.util.ext.registerTextChangeEvent
import com.sqsong.wanandroid.util.ext.setupUi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity @Inject constructor() : BaseActivity<RegisterPresenter>(), RegisterContract.View, IAppCompatActivity {

    @Inject
    lateinit var mProgressDialog: LoadingProgressDialog

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun initEvent() {
        setupUi(root)
        mPresenter.onAttach(this)
    }

    override fun getTitleText(): TextView {
        return registerTv
    }

    override fun backDisposable(): Disposable {
        return registerClickEvent(backIv) { finish() }
    }

    override fun userNameDisposable(): Disposable {
        return registerTextChangeEvent(userNameEdit, userNameInputLayout)
    }

    override fun passwordDisposable(): Disposable {
        return registerTextChangeEvent(passwordEdit, passwordInputLayout)
    }

    override fun confirmPasswordDisposable(): Disposable {
        return registerTextChangeEvent(confirmPassEdit, confirmPassInputLayout)
    }

    override fun commitObservable(): Observable<Any> {
        return RxView.clicks(registerBtn)
    }

    override fun userNameText(): String {
        return userNameEdit.text.toString().trim()
    }

    override fun passwordText(): String {
        return passwordEdit.text.toString().trim()
    }

    override fun confirmPasswordText(): String {
        return confirmPassEdit.text.toString().trim()
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
            3 -> {
                confirmPassInputLayout.isErrorEnabled = true
                confirmPassInputLayout.error = errorMsg
            }
        }
    }

    override fun showProcessDialog() {
        mProgressDialog.show(supportFragmentManager, "")
    }

    override fun hideProcessDialog() {
        mProgressDialog.dismiss()
    }

    override fun finishActivity() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}
