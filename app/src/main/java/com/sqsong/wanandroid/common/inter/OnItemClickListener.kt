package com.sqsong.wanandroid.common.inter

interface OnItemClickListener<T> {
    fun onItemClick(data: T?, position: Int)
}