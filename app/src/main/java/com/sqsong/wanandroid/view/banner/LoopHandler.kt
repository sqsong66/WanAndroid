package com.sqsong.wanandroid.view.banner

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

class LoopHandler(view: BannerView) : Handler() {

    private val mViewRef = WeakReference<BannerView>(view)

    override fun handleMessage(msg: Message?) {
        val bannerView = mViewRef.get() ?: return
        if (!bannerView.isLoop()) return
        when (msg?.what) {
            0 -> {
                bannerView.getViewPager().setCurrentItem(bannerView.getViewPager().currentItem + 1, true)
                bannerView.sendScrollMessage()
            }
            1 -> { // 防止在RecyclerView中重新看到banner页面时跳动
                bannerView.getViewPager().setCurrentItem(bannerView.getViewPager().currentItem - 1, false)
                bannerView.getViewPager().setCurrentItem(bannerView.getViewPager().currentItem + 1, false)
                bannerView.sendScrollMessage()
            }
        }

    }

}