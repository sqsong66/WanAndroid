package com.sqsong.wanandroid.mvp

import androidx.annotation.IdRes

interface IView {

    fun showLoading()

    fun hideLoading()

    fun showMessage(@IdRes messageId: Int)

    fun showMessage(message: String)

}