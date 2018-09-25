package com.sqsong.wanandroid.common.inter

interface IAppCompatActivity {
    /**
     * Get the content layout resource id.
     * @return layout resource id.
     */
    fun getLayoutResId(): Int

    /**
     * Start init something
     */
    fun initEvent()
}