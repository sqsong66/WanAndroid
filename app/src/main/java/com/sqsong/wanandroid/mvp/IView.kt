package com.sqsong.wanandroid.mvp

interface IView {

    fun showLoading()

    fun hideLoading()

    fun showMessage(message: String)

}