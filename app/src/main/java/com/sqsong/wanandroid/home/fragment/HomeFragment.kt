package com.sqsong.wanandroid.home.fragment

import android.view.View
import com.google.gson.GsonBuilder
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseFragment
import com.sqsong.wanandroid.base.HomeBannerBean
import com.sqsong.wanandroid.util.SnackbarUtil
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_news.*
import javax.inject.Inject

class HomeFragment @Inject constructor() : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        SnackbarUtil.showToastText(context!!, "Star")
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(view: View) {
        collection_iv.setOnClickListener(this)
    }

    override fun initEvent() {
        val json = "{\"data\":[{\"desc\":\"一起来做个App吧\",\"id\":10,\"imagePath\":\"http://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png\",\"isVisible\":1,\"order\":2,\"title\":\"一起来做个App吧\",\"type\":0,\"url\":\"http://www.wanandroid.com/blog/show/2\"},{\"desc\":\"\",\"id\":4,\"imagePath\":\"http://www.wanandroid.com/blogimgs/ab17e8f9-6b79-450b-8079-0f2287eb6f0f.png\",\"isVisible\":1,\"order\":0,\"title\":\"看看别人的面经，搞定面试~\",\"type\":1,\"url\":\"http://www.wanandroid.com/article/list/0?cid=73\"},{\"desc\":\"\",\"id\":3,\"imagePath\":\"http://www.wanandroid.com/blogimgs/fb0ea461-e00a-482b-814f-4faca5761427.png\",\"isVisible\":1,\"order\":1,\"title\":\"兄弟，要不要挑个项目学习下?\",\"type\":1,\"url\":\"http://www.wanandroid.com/project\"},{\"desc\":\"\",\"id\":6,\"imagePath\":\"http://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png\",\"isVisible\":1,\"order\":1,\"title\":\"我们新增了一个常用导航Tab~\",\"type\":1,\"url\":\"http://www.wanandroid.com/navi\"},{\"desc\":\"\",\"id\":2,\"imagePath\":\"http://www.wanandroid.com/blogimgs/90cf8c40-9489-4f9d-8936-02c9ebae31f0.png\",\"isVisible\":1,\"order\":2,\"title\":\"JSON工具\",\"type\":1,\"url\":\"http://www.wanandroid.com/tools/bejson\"},{\"desc\":\"\",\"id\":5,\"imagePath\":\"http://www.wanandroid.com/blogimgs/acc23063-1884-4925-bdf8-0b0364a7243e.png\",\"isVisible\":1,\"order\":3,\"title\":\"微信文章合集\",\"type\":1,\"url\":\"http://www.wanandroid.com/blog/show/6\"}],\"errorCode\":0,\"errorMsg\":\"\"}"
        val gson = GsonBuilder().create()
        val bannerBean = gson.fromJson<HomeBannerBean>(json, HomeBannerBean::class.java)
        bannerView.postDelayed({ bannerView?.setBannerData(bannerBean.data) }, 1000)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) bannerView?.startLoop() else bannerView?.stopLoop()
    }

}
