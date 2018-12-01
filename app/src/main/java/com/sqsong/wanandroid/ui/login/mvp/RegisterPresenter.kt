package com.sqsong.wanandroid.ui.login.mvp

import android.content.Context
import android.content.SharedPreferences
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RegisterPresenter @Inject constructor(private val registerView: RegisterContract.View,
                                            private val registerModel: RegisterModel,
                                            private val disposable: CompositeDisposable) :
        BasePresenter<RegisterContract.View, RegisterContract.Model>(registerModel, disposable) {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mPreferences: SharedPreferences

    override fun onAttach(view: RegisterContract.View) {
        mView = registerView
        registerEvents()
    }

    private fun registerEvents() {
        // Set title font.
        val languageType = mPreferences[Constants.LANGUAGE_TYPE, 0]
        CommonUtil.setAssetsTextFont(mView.getTitleText(), languageType)
        disposable.add(mView.backDisposable())
        disposable.add(mView.userNameDisposable())
        disposable.add(mView.passwordDisposable())
        disposable.add(mView.confirmPasswordDisposable())
        registerCommitEvent()
    }

    private fun registerCommitEvent() {
        mView.commitObservable()
                .filter {
                    return@filter validParams(mView.userNameText(),
                            mView.passwordText(), mView.confirmPasswordText())
                }
                .doOnNext { mView.showProcessDialog() }
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .switchMap {
                    return@switchMap registerModel.register(mView.userNameText(),
                            mView.passwordText(), mView.confirmPasswordText())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach { mView.hideProcessDialog() }
                .subscribe(object : ObserverImpl<BaseData>(disposable) {
                    override fun onSuccess(bean: BaseData) {
                        if (bean.errorCode == 0) {
                            mView.showMessage(mContext.getString(R.string.text_register_success))
                            mView.finishActivity()
                        } else {
                            mView.showMessage(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        registerCommitEvent()
                        mView.showMessage(error.showMessage)
                    }
                })
    }

    private fun validParams(userNameText: String, passwordText: String,
                            confirmPasswordText: String): Boolean {
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
            (confirmPasswordText.isEmpty() || confirmPasswordText != passwordText) -> {
                mView.showInputLayoutError(3, mContext.getString(R.string.text_confirm_password_must_same))
                return false
            }
            else -> true
        }
    }

}