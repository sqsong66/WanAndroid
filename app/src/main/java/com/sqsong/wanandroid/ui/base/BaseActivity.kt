package com.sqsong.wanandroid.ui.base

import com.sqsong.wanandroid.mvp.IPresenter
import dagger.android.support.DaggerAppCompatActivity

class BaseActivity<P : IPresenter<*>> : DaggerAppCompatActivity() {

    var mPresenter: P? = null

}
