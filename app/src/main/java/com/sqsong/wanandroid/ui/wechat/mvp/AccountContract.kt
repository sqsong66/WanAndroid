package com.sqsong.wanandroid.ui.wechat.mvp

import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

/**
 * 公众号Fragment Contract
 */
interface AccountContract {

    interface View : IView {
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
    }

    interface Model : IModel {
        fun getPublicAccountArticleList(id: Int, page: Int): Observable<HomeItemBean>
    }

}