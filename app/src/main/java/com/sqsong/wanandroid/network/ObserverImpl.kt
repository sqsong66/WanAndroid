package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseApplication
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

abstract class ObserverImpl<T>(private val disposable: CompositeDisposable? = null) : Observer<T> {

    abstract fun onSuccess(data: T)

    abstract fun onFail(error: ApiException)

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        onFail(ApiException.parseException(e))
    }

    override fun onSubscribe(d: Disposable) {
        disposable?.add(d)
    }
}

class ApiException(val code: Int, override val message: String?, val showMessage: String) : Exception() {

    companion object {

        const val ERROR_UNKONWN = -1
        const val ERROR_UNKONWN_HOST = -2
        const val ERROR_CONNECT_TIMEOUT = -3

        fun parseException(error: Throwable): ApiException {
            if (error is ApiException) return error

            val context = BaseApplication.INSTANCE.applicationContext
            var showMessage: String
            var code: Int
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
                showMessage = context.getString(R.string.text_unknown_error)
            }
            return ApiException(code, error.message, showMessage)
        }
    }

}