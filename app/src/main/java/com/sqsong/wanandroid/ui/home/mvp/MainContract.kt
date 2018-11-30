package com.sqsong.wanandroid.ui.home.mvp

import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sqsong.wanandroid.data.HotSearchBean
import com.sqsong.wanandroid.data.HotSearchData
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.mvp.IView
import io.reactivex.Observable

interface MainContract {

    interface View : IView {
        fun getFab(): FloatingActionButton
        fun getCurrentIndex(): Int
        fun supportFragmentManager(): FragmentManager
        fun setPagerAdapter(adapter: FragmentStatePagerAdapter)
        fun startNewActivity(intent: Intent)
        fun showLoginOutTipDialog()
        fun switchBottomViewNavigation(@IdRes id: Int)
        fun showUserName(userName: String?)
        fun getActivity(): AppCompatActivity
        fun setupHotSearchKey(keyList: List<HotSearchData>?)
        fun showCameraPermissionDialog()
    }

    interface Model : IModel {
        fun getHotKey(): Observable<HotSearchBean>
    }

}