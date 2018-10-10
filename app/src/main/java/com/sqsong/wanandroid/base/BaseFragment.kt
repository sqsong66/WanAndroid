package com.sqsong.wanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import butterknife.ButterKnife
import butterknife.Unbinder
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {

    private lateinit var mUnBinder: Unbinder

    @LayoutRes
    abstract fun getLayoutResId(): Int

    abstract fun initView(view: View)

    abstract fun initEvent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUnBinder = ButterKnife.bind(this, view)
        initView(view)
        initEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnBinder.unbind()
    }
}