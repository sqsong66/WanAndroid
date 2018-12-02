package com.sqsong.wanandroid.ui.settings

import android.content.SharedPreferences
import android.view.MenuItem
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.settings.dialog.LanguageSettingDialog
import com.sqsong.wanandroid.ui.settings.mvp.SettingContract
import com.sqsong.wanandroid.ui.settings.mvp.SettingPresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.ext.clickObservable
import com.sqsong.wanandroid.util.ext.setupToolbar
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_setting.*
import javax.inject.Inject

class SettingActivity : BaseActivity<SettingPresenter>(), SettingContract.View, LanguageSettingDialog.OnLanguageChangeListener {

    @Inject
    lateinit var mLanguageSettingDialog: LanguageSettingDialog

    @Inject
    lateinit var mThemeDialog: ThemeSwitcherDialog

    @Inject
    lateinit var mPreferences: SharedPreferences

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
        val type = mPreferences[Constants.LANGUAGE_TYPE, 0]
        if (languageType == type) return
        BaseApplication.INSTANCE.changeLanguage(languageType)
    }

    override fun showThemeDialog() {
        mThemeDialog.show(supportFragmentManager, "")
    }
}