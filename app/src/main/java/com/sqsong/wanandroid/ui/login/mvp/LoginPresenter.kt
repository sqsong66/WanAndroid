package com.sqsong.wanandroid.ui.login.mvp

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.LoginBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.PreferenceHelper.set
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val loginView: LoginContract.View,
                                         private val loginModel: LoginModel,
                                         private val disposable: CompositeDisposable) :
        BasePresenter<LoginContract.View, LoginModel>(loginModel, disposable) {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mPreferences: SharedPreferences

    override fun onAttach(view: LoginContract.View) {
        mView = loginView
        registerEvents()
    }

    private fun registerEvents() {
        setupUserName()
        val languageType = mPreferences[Constants.LANGUAGE_TYPE, 0]
        CommonUtil.setAssetsTextFont(mView.getTitleText(), languageType)
        disposable.add(mView.closeDisposable())
        disposable.add(mView.forgetPasswordDisposable())
        disposable.add(mView.registerDisposable())
        disposable.add(mView.userNameDisposable())
        disposable.add(mView.passwordDisposable())
        registerCommitEvent()
    }

    private fun setupUserName() {
        mPreferences[Constants.LOGIN_USER_NAME] = "" // Clear login user name.
        val userName = mPreferences[Constants.LOGIN_LATEST_USER] ?: ""
        if (!TextUtils.isEmpty(userName)) {
            mView.inflateUserName(userName)
        }
    }

    private fun registerCommitEvent() {
        mView.commitObservable()
                .filter {
                    return@filter validParams(mView.userNameText(), mView.passwordText())
                }
                .doOnNext { mView.showProcessDialog() }
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .switchMap {
                    return@switchMap loginModel.login(mView.userNameText(), mView.passwordText())
                }
                .observeOn(AndroidSchedulers.mainThread())
                // need add retry. if not, while meet an error, the observable will dispose, and while no longer get the view click event.
                // retry operator will not go to observer's onError method(we cannot process the exceptions).
                // .retry()
                .doOnEach {
                    mView.hideProcessDialog()
                }
                .subscribe(object : ObserverImpl<LoginBean>(disposable) {
                    override fun onSuccess(bean: LoginBean) {
                        if (bean.errorCode == 0) {
                            mPreferences[Constants.LOGIN_USER_NAME] = bean.data?.username
                            mPreferences[Constants.LOGIN_LATEST_USER] = bean.data?.username
                            mView.startHomeActivity()
                        } else {
                            mView.showMessage(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        registerCommitEvent() // Register button's click event again, or it's listener = null.
                        mView.showMessage(error.showMessage)
                    }
                })
    }

    private fun validParams(userNameText: String, passwordText: String): Boolean {
        return when {
            userNameText.isEmpty() -> {
                mView.showInputLayoutError(0, mContext.getString(R.string.text_user_name_empty))
                return false
            }
            passwordText.isEmpty() -> {
                mView.showInputLayoutError(1, mContext.getString(R.string.text_password_empty))
                return false
            }
            passwordText.length < 6 -> {
                mView.showInputLayoutError(2, mContext.getString(R.string.text_password_too_short))
                return false
            }
            else -> true
        }
    }

}