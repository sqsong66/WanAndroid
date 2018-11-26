package com.sqsong.wanandroid.ui.preview

import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.FragmentPagerAdapter
import com.sqsong.wanandroid.data.WelfareData
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.preview.mvp.ImagePreviewContract
import com.sqsong.wanandroid.ui.preview.mvp.ImagePreviewPresenter
import com.sqsong.wanandroid.util.Constants
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreviewActivity : BaseActivity<ImagePreviewPresenter>(), ImagePreviewContract.View {

    private var mCurPos = 0
    private var mWelfareList: List<WelfareData>? = null

    override fun beforeInflateView() {
        mCurPos = intent.getIntExtra(Constants.IMAGE_POSITION, 0)
        mWelfareList = intent.getParcelableArrayListExtra(Constants.IMAGE_LIST)
        if (mWelfareList == null || mWelfareList!!.isEmpty()) {
            showMessage(getString(R.string.text_empty_image_list))
            finish()
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_image_preview

    override fun initEvent() {
        mPresenter.onAttach(this)
        indexTv.text = String.format(getString(R.string.text_image_index), mCurPos + 1, mWelfareList?.size)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                indexTv.text = String.format(getString(R.string.text_image_index), position + 1, mWelfareList?.size)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    override fun getWelfareList(): List<WelfareData> = mWelfareList!!

    override fun fragmentManager(): FragmentManager = supportFragmentManager

    override fun setupViewPagerAdapter(pagerAdapter: FragmentPagerAdapter) {
        viewPager.adapter = pagerAdapter
        viewPager.setCurrentItem(mCurPos, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }
}