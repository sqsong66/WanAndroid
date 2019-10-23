package com.sqsong.wanandroid.ui.login

import android.content.Intent
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.LoadingProgressDialog
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.home.activity.MainActivity
import com.sqsong.wanandroid.ui.login.mvp.LoginContract
import com.sqsong.wanandroid.ui.login.mvp.LoginPresenter
import com.sqsong.wanandroid.util.ext.registerClickEvent
import com.sqsong.wanandroid.util.ext.registerTextChangeEvent
import com.sqsong.wanandroid.util.ext.setupUi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

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

    override fun forgetPasswordDisposable(): Disposable {
        return registerClickEvent(forgetPasswordTv) { showMessage(getString(R.string.text_developing)) }
    }

    override fun registerDisposable(): Disposable {
        return registerClickEvent(registerTipsTv) { startActivity(Intent(this, RegisterActivity::class.java)) }
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

    override fun inflateUserName(userName: String) {
        userNameEdit.setText(userName)
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
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, loginBtn, "transition")
        val revealX = (loginBtn.x + loginBtn.width / 2).toInt()
        val revealY = (loginBtn.y + loginBtn.height / 2).toInt()
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("revealX", revealX)
            putExtra("revealY", revealY)
        }
        ActivityCompat.startActivity(this, intent, options.toBundle())
        // startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
