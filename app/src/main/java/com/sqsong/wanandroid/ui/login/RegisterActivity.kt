package com.sqsong.wanandroid.ui.login

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.login.mvp.RegisterContract
import com.sqsong.wanandroid.ui.login.mvp.RegisterPresenter
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.registerClickEvent
import com.sqsong.wanandroid.util.registerTextChangeEvent
import com.sqsong.wanandroid.util.setupUi
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

@ChangeThemeAnnotation
class RegisterActivity @Inject constructor() : BaseActivity<RegisterPresenter>(), RegisterContract.View, IAppCompatActivity {

    override fun initEvent() {
        setupUi(root)
        mPresenter.onAttach(this)
        CommonUtil.setAssetsTextFont(registerTv, "font/Pacifico-Regular.ttf")
    }

    override fun backDisposable(): Disposable {
        return registerClickEvent(backIv) { finish() }
    }

    override fun userNameDisposable(): Disposable {
        return registerTextChangeEvent(userNameEdit, userNameInputLayout)
    }

    override fun passwordDisposable(): Disposable {
        return registerTextChangeEvent(passwordEdit, passwordInputLayout)
    }

    override fun confirmPasswordDisposable(): Disposable {
        return registerTextChangeEvent(confirmPassEdit, confirmPassInputLayout)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }
}
