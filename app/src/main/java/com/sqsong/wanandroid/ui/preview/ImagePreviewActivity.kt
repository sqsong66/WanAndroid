package com.sqsong.wanandroid.ui.preview

import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.DepthPageTransformer
import com.sqsong.wanandroid.common.FragmentPagerAdapter
import com.sqsong.wanandroid.common.inter.TranslucentNavigation
import com.sqsong.wanandroid.data.WelfareData
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.preview.mvp.ImagePreviewContract
import com.sqsong.wanandroid.ui.preview.mvp.ImagePreviewPresenter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.ext.setupToolbar
import kotlinx.android.synthetic.main.activity_image_preview.*

@TranslucentNavigation
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
        setupToolbar(toolbar)
        mPresenter.onAttach(this)
        (toolbar.layoutParams as ConstraintLayout.LayoutParams).topMargin = DensityUtil.getStatusBarHeight(this)
        toolbar.post { toolbar.title = String.format(getString(R.string.text_image_index), mCurPos + 1, mWelfareList?.size) }
        viewPager.setPageTransformer(true, DepthPageTransformer())
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                toolbar.title = String.format(getString(R.string.text_image_index), position + 1, mWelfareList?.size)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
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