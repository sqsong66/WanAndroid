package com.sqsong.wanandroid.network

import android.content.Intent
import android.widget.Toast
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.BaseBean
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.util.LogUtil
import com.sqsong.wanandroid.util.NetworkUtil
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

abstract class ObserverImpl<T : BaseBean<*>>(private val disposable: CompositeDisposable? = null) : Observer<T> {

    abstract fun onSuccess(bean: T)

    abstract fun onFail(error: ApiException)

    override fun onSubscribe(d: Disposable) {
        disposable?.add(d)
    }

    override fun onNext(t: T) {
        if (t.errorCode == -1001) {
            onError(ApiException(ApiException.ERROR_TOKEN_EXPIRED,
                    t.errorMsg, BaseApplication.INSTANCE.applicationContext.getString(R.string.text_token_expired)))
            return
        }
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        LogUtil.e("onError", e.message ?: "onError")
        val apiException = ApiException.parseException(e)
        if (apiException.errorCode == ApiException.ERROR_TOKEN_EXPIRED) {
            quitAndStartLogin(apiException.showMessage)
            return
        }
        onFail(apiException)
    }

    override fun onComplete() {
        LogUtil.e("onComplete")
    }

    private fun quitAndStartLogin(showMessage: String) {
        BaseApplication.INSTANCE.quitApp()
        val context = BaseApplication.INSTANCE.applicationContext
        Toast.makeText(context, showMessage, Toast.LENGTH_SHORT).show()
        CookieManager.getInstance(context).clearCookieInfo()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}

class ApiException(val errorCode: Int, override val message: String?, val showMessage: String) : Exception() {

    companion object {

        const val ERROR_UNKONWN = -1
        const val ERROR_UNKONWN_HOST = -2
        const val ERROR_CONNECT_TIMEOUT = -3
        const val ERROR_TOKEN_EXPIRED = -4

        fun parseException(error: Throwable): ApiException {
            if (error is ApiException) return error

            val context = BaseApplication.INSTANCE.applicationContext
            val showMessage: String
            val code: Int
            if (error is UnknownHostException || error is HttpException) {
                code = ERROR_UNKONWN_HOST
                showMessage = if (!NetworkUtil.isNetworkAvaiable(context)) {
                    context.getString(R.string.text_network_not_available)
                } else {
                    context.getString(R.string.text_address_not_reachable)
                }
            } else if (error is ConnectException || error is TimeoutException
                    || error is SocketException || error is SocketTimeoutException) {
                code = ERROR_CONNECT_TIMEOUT
                showMessage = context.getString(R.string.text_network_timeout)
            } else {
                code = ERROR_UNKONWN
                showMessage = error.message ?: context.getString(R.string.text_unknown_error)
            }
            return ApiException(code, error.message, showMessage)
        }
    }

}