package com.sqsong.wanandroid.common

interface OnItemClickListener<T> {
    fun onItemClick(data: T?, position: Int)
}