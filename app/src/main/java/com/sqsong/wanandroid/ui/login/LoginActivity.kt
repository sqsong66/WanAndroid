package com.sqsong.wanandroid.ui.login

import android.content.Intent
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.LoadingProgressDialog
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.network.ApiService
import com.sqsong.wanandroid.ui.home.HomeActivity
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.setupUi
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ChangeThemeAnnotation
class LoginActivity : DaggerAppCompatActivity(), IAppCompatActivity {

    @Inject
    lateinit var mDisposable: CompositeDisposable

    @Inject
    lateinit var mApiService: ApiService

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initEvent() {
        setupUi(root)
        registerEvents()
    }

    private fun registerEvents() {
        CommonUtil.setAssetsTextFont(loginTv, "font/Pacifico-Regular.ttf")
        registerClickEvent(closeIv) { finish() }
        registerClickEvent(registerTipsTv) { startActivity(Intent(this, RegisterActivity::class.java)) }
        registerClickEvent(forgetPasswordTv) { v -> Snackbar.make(v, R.string.text_developing, Snackbar.LENGTH_SHORT).show() }
        registerTextChangeEvent(userNameEdit, userNameInputLayout)
        registerTextChangeEvent(passwordEdit, passwordInputLayout)

        /*RxView.clicks(loginBtn)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .map {
                    return@map validParams()
                }.switchMap {
                    return@switchMap if (it) mApiService.getHomeBanner() else Observable.empty<HomeBannerBean>()
                }.subscribe {

                }*/
        val dialog = LoadingProgressDialog.newInstance("登录中...")
        mDisposable.add(RxView.clicks(loginBtn)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    dialog.show(supportFragmentManager, "")
                }
                .map {
                    return@map validParams()
                }
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribe {
                    dialog.dismiss()
                    if (it) startActivity(Intent(this, HomeActivity::class.java))
                })
    }

    private fun validParams(): Boolean {
        val userName = userNameEdit.text.toString().trim()
        val password = passwordEdit.text.toString().trim()
        return when {
            userName.isEmpty() -> {
                userNameInputLayout.isErrorEnabled = true
                userNameInputLayout.error = getString(R.string.text_user_name_empty)
                false
            }
            password.isEmpty() -> {
                passwordInputLayout.isErrorEnabled = true
                passwordInputLayout.error = getString(R.string.text_password_empty)
                false
            }
            password.length < 6 -> {
                passwordInputLayout.isErrorEnabled = true
                passwordInputLayout.error = getString(R.string.text_password_too_short)
                false
            }
            else -> true
        }
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
