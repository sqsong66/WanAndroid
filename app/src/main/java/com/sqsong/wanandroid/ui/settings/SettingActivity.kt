package com.sqsong.wanandroid.ui.settings

import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.settings.mvp.SettingContract
import com.sqsong.wanandroid.ui.settings.mvp.SettingPresenter
import com.sqsong.wanandroid.util.LogUtil
import com.sqsong.wanandroid.util.ext.clickObservable
import com.sqsong.wanandroid.util.ext.setupToolbar
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_setting.*

@ChangeThemeAnnotation
class SettingActivity : BaseActivity<SettingPresenter>(), SettingContract.View {

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

    override fun showLanguageDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.text_choose_language)
                .setCancelable(false)
                .setSingleChoiceItems(R.array.language_array, 0) { dialog, which ->
                    run {
                        LogUtil.e("Click item: $which")
                    }
                }
                .setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.text_sure) { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }
                .show()
    }
}