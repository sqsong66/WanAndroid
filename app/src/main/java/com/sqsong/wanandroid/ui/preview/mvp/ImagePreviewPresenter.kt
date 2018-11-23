package com.sqsong.wanandroid.ui.preview.mvp

import androidx.fragment.app.Fragment
import com.sqsong.wanandroid.common.FragmentPagerAdapter
import com.sqsong.wanandroid.data.WelfareData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.ui.preview.fragment.PreviewFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ImagePreviewPresenter @Inject constructor(private val disposable: CompositeDisposable) :
        BasePresenter<ImagePreviewContract.View, IModel>(null, disposable) {

    override fun onAttach(view: ImagePreviewContract.View) {
        super.onAttach(view)
        init()
    }

    private fun init() {
        preparePreviewFragments(mView.getWelfareList())
    }

    private fun preparePreviewFragments(imageList: List<WelfareData>) {
        val fragmentList = mutableListOf<Fragment>()
        for (welfareData in imageList) {
            val fragment = PreviewFragment.newInstance(welfareData.url)
            fragment.setPresenter(PreviewPresenter(disposable))
            fragmentList.add(fragment)
        }
        val pagerAdapter = FragmentPagerAdapter(mView.fragmentManager(), fragmentList)
        mView.setupViewPagerAdapter(pagerAdapter)
    }

}