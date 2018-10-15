package com.sqsong.wanandroid.mvp

interface IPresenter<V: IView> {

    fun onAttach(view: V)

    fun onDestroy()

}