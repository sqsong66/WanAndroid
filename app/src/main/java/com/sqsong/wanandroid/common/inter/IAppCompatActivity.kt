package com.sqsong.wanandroid.common.inter

import androidx.annotation.LayoutRes

interface IAppCompatActivity {

    /**
     * before inflate view do something, like get intent data.
     */
    fun beforeInflateView()

    /**
     * Get the content layout resource id.
     * @return layout resource id.
     */
    @LayoutRes
    fun getLayoutResId(): Int

    /**
     * Start init something
     */
    fun initEvent()

}