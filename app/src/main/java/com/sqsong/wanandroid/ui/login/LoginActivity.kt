package com.sqsong.wanandroid.ui.login

import android.content.Intent
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.ui.home.HomeActivity
import com.sqsong.wanandroid.util.setupUi
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@ChangeThemeAnnotation
class LoginActivity : DaggerAppCompatActivity(), IAppCompatActivity {

    @Inject
    lateinit var mDisposable: CompositeDisposable

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initEvent() {
        setupUi(root)
        registerEvents()
    }

    private fun registerEvents() {
        registerClickEvent(closeIv) { finish() }
        registerClickEvent(registerTipsTv) { startActivity(Intent(this, RegisterActivity::class.java)) }
        registerClickEvent(forgetPasswordTv) { v -> Snackbar.make(v, R.string.text_developing, Snackbar.LENGTH_SHORT).show() }
        registerTextChangeEvent(userNameEdit, userNameInputLayout)
        registerTextChangeEvent(passwordEdit, passwordInputLayout)

        mDisposable.add(RxView.clicks(loginBtn)
                .map {
                    val userName = userNameEdit.text.toString().trim()
                    val password = passwordEdit.text.toString().trim()
                    when {
                        userName.isEmpty() -> return@map -1
                        password.isEmpty() -> return@map -2
                        password.length < 6 -> return@map -3
                        else -> return@map 0
                    }
                }.subscribe {
                    /*when (it) {
                        -1 -> {
                            userNameInputLayout.isErrorEnabled = true
                            userNameInputLayout.error = getString(R.string.text_user_name_empty)
                        }
                        -2 -> {
                            passwordInputLayout.isErrorEnabled = true
                            passwordInputLayout.error = getString(R.string.text_password_empty)
                        }
                        -3 -> {
                            passwordInputLayout.isErrorEnabled = true
                            passwordInputLayout.error = getString(R.string.text_password_too_short)
                        }
                        else -> startActivity(Intent(this, HomeActivity::class.java))
                    }*/
                    startActivity(Intent(this, HomeActivity::class.java))
                })
    }

    private inline fun registerClickEvent(view: View, crossinline action: (view: View) -> Unit) {
        mDisposable.add(RxView.clicks(view)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { action(view) }
        )
    }

    private fun registerTextChangeEvent(editText: EditText, inputLayout: TextInputLayout) {
        mDisposable.add(RxTextView.textChanges(editText)
                .subscribe {
                    if (it.toString().isNotEmpty() && inputLayout.isErrorEnabled) {
                        inputLayout.isErrorEnabled = false
                    }
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.dispose()
    }
}
