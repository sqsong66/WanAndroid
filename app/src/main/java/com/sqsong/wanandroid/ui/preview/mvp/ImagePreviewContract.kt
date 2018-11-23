package com.sqsong.wanandroid.ui.preview.mvp

import androidx.fragment.app.FragmentManager
import com.sqsong.wanandroid.common.FragmentPagerAdapter
import com.sqsong.wanandroid.data.WelfareData
import com.sqsong.wanandroid.mvp.IView

interface ImagePreviewContract {

    interface View : IView {
        fun getWelfareList(): List<WelfareData>
        fun fragmentManager(): FragmentManager
        fun setupViewPagerAdapter(pagerAdapter: FragmentPagerAdapter)
    }

}