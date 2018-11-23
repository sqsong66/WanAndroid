package com.sqsong.wanandroid.common.inter

import android.view.View

interface OnViewItemClickListener<T> {
    fun onItemClick(view: View, data: T?, position: Int)
}