package com.sqsong.wanandroid.ui.home.mvp.knowledge

import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

interface ChiledKnowledgeContract {

    interface View: IView {

    }

    interface Model: IModel {
        fun getKnowledgeChildList(page: Int, cid: Int): Observable<HomeItemBean>
    }

}