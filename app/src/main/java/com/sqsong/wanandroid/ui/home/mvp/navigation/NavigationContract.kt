package com.sqsong.wanandroid.ui.home.mvp.navigation

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.wanandroid.data.NavigationBean
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import com.sqsong.wanandroid.ui.home.adapter.NavigationAdapter
import io.reactivex.Observable

interface NavigationContract {

    interface View : IView {
        fun getFragmentContext(): Context
        fun getRecycler(): RecyclerView
        fun setRecyclerAdapter(adapter: NavigationAdapter, itemDecoration: RecyclerView.ItemDecoration)
        fun showEmptyPage()
        fun showLoadingPage()
        fun showContentPage()
        fun showErrorPage()
        fun scrollRecycler(position: Int)
        fun startNewActivity(intent: Intent)
    }

    interface Model : IModel {
        fun getNavigationList(): Observable<NavigationBean>
    }

}