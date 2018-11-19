package com.sqsong.wanandroid.ui.settings.mvp

import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

interface SettingContract {

    interface View : IView {
        fun languageClickObservable(): Observable<Any>
        fun themeClickObservable(): Observable<Any>
        fun showLanguageDialog()
        fun showThemeDialog()
    }

}