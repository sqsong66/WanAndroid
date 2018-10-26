package com.sqsong.wanandroid.common

import android.os.Handler
import android.os.Message
import com.sqsong.wanandroid.mvp.IView
import java.lang.ref.WeakReference

class NoLeakHandler<T : IView>(activity: T) : Handler() {

    private var mReference: WeakReference<T>? = WeakReference(activity)

    override fun handleMessage(msg: Message) {
        mReference?.get()?.handleMessage(msg)
    }

}
