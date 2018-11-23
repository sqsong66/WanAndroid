package com.sqsong.wanandroid.ui.preview.mvp

import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

interface PreviewContract {

    interface View : IView {
        fun imageClickObservable(): Observable<Any>
        fun finishActivity()
    }

}