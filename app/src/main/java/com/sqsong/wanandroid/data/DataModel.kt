package com.sqsong.wanandroid.data

abstract class BaseBean<T> {
    val errorCode: Int = 0
    val errorMsg: String? = null
    abstract var data: T
}

/********************* Begin: Home banner bean ***********************/
data class HomeBannerBean(override var data: MutableList<HomeBannerData>) : BaseBean<MutableList<HomeBannerData>>()

data class HomeBannerData(val desc: String,
                          val id: Int,
                          val imagePath: String,
                          val isVisible: Int,
                          val order: Int,
                          val title: String,
                          val type: Int,
                          val url: String)
/********************* End: Home banner bean ***********************/