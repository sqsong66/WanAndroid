package com.sqsong.wanandroid.ui.settings

import android.view.MenuItem
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.settings.dialog.LanguageSettingDialog
import com.sqsong.wanandroid.ui.settings.mvp.SettingContract
import com.sqsong.wanandroid.ui.settings.mvp.SettingPresenter
import com.sqsong.wanandroid.util.ext.clickObservable
import com.sqsong.wanandroid.util.ext.setupToolbar
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_setting.*
import javax.inject.Inject

@ChangeThemeAnnotation
class SettingActivity : BaseActivity<SettingPresenter>(), SettingContract.View, LanguageSettingDialog.OnLanguageChangeListener {

    @Inject
    lateinit var mLanguageSettingDialog: LanguageSettingDialog

    @Inject
    lateinit var mThemeDialog: ThemeSwitcherDialog

    override fun getLayoutResId(): Int = R.layout.activity_setting

    override fun initEvent() {
        setupToolbar(toolbar)
        mPresenter.onAttach(this)
        toolbar.post { toolbar.title = getString(R.string.text_settings) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun languageClickObservable(): Observable<Any> = clickObservable(R.id.language_setting_ll)

    override fun themeClickObservable(): Observable<Any> = clickObservable(R.id.theme_setting_ll)

    override fun showLanguageDialog() {
        mLanguageSettingDialog.show(supportFragmentManager, "")
    }

    override fun languageChanged(languageType: Int) {

    }

    override fun showThemeDialog() {
        mThemeDialog.show(supportFragmentManager, "")
    }
}