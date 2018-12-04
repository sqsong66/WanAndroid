package com.sqsong.wanandroid.mvp

import android.os.Message

interface IView {

    fun showLoading()

    fun hideLoading()

    fun showMessage(message: String?)

    fun handleMessage(message: Message)

    fun getStringFromResource(strId: Int): String

}