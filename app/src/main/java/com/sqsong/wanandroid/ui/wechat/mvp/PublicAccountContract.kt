package com.sqsong.wanandroid.ui.wechat.mvp

import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

interface PublicAccountContract {

    interface View : IView {
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
    }

    interface Model : IModel {
        fun getPublicAccountPeople(): Observable<KnowledgeBean>
    }

}