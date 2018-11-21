package com.sqsong.wanandroid.network

import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.BaseBean
import com.sqsong.wanandroid.data.GankBasedBean
import io.reactivex.disposables.CompositeDisposable

abstract class ObserverImpl<T : BaseBean<*>>(private val disposable: CompositeDisposable? = null) : AbstractObserver<T>() {

    override fun onNext(t: T) {
        if (t.errorCode == -1001) {
            onError(ApiException(ApiException.ERROR_TOKEN_EXPIRED,
                    t.errorMsg, BaseApplication.INSTANCE.applicationContext.getString(R.string.text_token_expired)))
            return
        }
        onSuccess(t)
    }

}

abstract class GankObserverImpl<T : GankBasedBean<*>>(private val disposable: CompositeDisposable? = null) : AbstractObserver<T>()