package com.sqsong.wanandroid.ui.base

import com.sqsong.wanandroid.mvp.IPresenter
import javax.inject.Inject

abstract class BaseInjectFragment<P : IPresenter<*>> : BaseFragment() {

    @Inject
    lateinit var mPresenter: P

}