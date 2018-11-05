package com.sqsong.wanandroid.ui.base

import com.sqsong.wanandroid.mvp.IPresenter
import javax.inject.Inject

abstract class LazyLoadInjectFragment<P : IPresenter<*>> : LazyLoadFragment() {

    @Inject
    lateinit var mPresenter: P

}
