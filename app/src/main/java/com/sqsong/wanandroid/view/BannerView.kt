package com.sqsong.wanandroid.view

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.HomeBannerData
import com.sqsong.wanandroid.common.GlideApp
import com.sqsong.wanandroid.home.adapter.BannerPagerAdapter
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.transformer.ZoomPageTransformer

class BannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    private var duration: Int = 0
    private var loop: Boolean = false
    private var pagerScrollDuration: Int = 0
    private var showIndicator: Boolean = false

    private val mHandler = Handler()
    private var viewPager: ViewPager? = null
    private var defaultImage: ImageView? = null
    private var indicator: CirclePagerIndicator? = null
    private var mBannerAdapter: BannerPagerAdapter? = null
    private var mBannerList = mutableListOf<HomeBannerData>()

    private val mLoopRunnable = object : Runnable {
        override fun run() {
            val currentItem: Int = viewPager?.currentItem!!
            viewPager?.setCurrentItem(currentItem + 1, true)
            mHandler.postDelayed(this, duration.toLong())
        }
    }

    init {
        initParams(context, attrs)
        initView(context)
    }

    private fun initParams(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePagerIndicator)
        loop = typedArray.getBoolean(R.styleable.BannerView_loop, true)
        duration = typedArray.getInteger(R.styleable.BannerView_duration, 5000)
        showIndicator = typedArray.getBoolean(R.styleable.BannerView_showIndicator, true)
        pagerScrollDuration = typedArray.getInteger(R.styleable.BannerView_pagerScrollDuration, 500)
        typedArray.recycle()
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_banner, this, false)
        addView(view)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(150))

        viewPager = view.findViewById(R.id.viewPager)
        indicator = view.findViewById(R.id.indicator)
        defaultImage = view.findViewById(R.id.default_iv)
        if (showIndicator) {
            indicator?.visibility = View.VISIBLE
        } else {
            indicator?.visibility = View.GONE
        }

        setViewPagerScrollDuration()
        mBannerAdapter = BannerPagerAdapter(context, mBannerList)
        viewPager?.adapter = mBannerAdapter
        viewPager?.setPageTransformer(true, ZoomPageTransformer(viewPager!!))
        viewPager?.pageMargin = DensityUtil.dip2px(10)

        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(DensityUtil.dip2px(5)))
        GlideApp.with(this)
                .load(R.drawable.placeholder)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(transformation))
                .into(defaultImage!!)
        GlideApp.with(this)
                .asBitmap()
                .load(R.drawable.placeholder)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val width = resource.width
                        val height = resource.height
                        val params = defaultImage?.layoutParams as ConstraintLayout.LayoutParams
                        val resultWidth = DensityUtil.getScreenWidth() - params.leftMargin - params.rightMargin
                        val resultHeight = height * resultWidth / width
                        params.height = resultHeight
                        layoutParams.height = resultHeight + params.topMargin + params.bottomMargin
                    }
                })
    }

    private fun setViewPagerScrollDuration() {
        val scroller = ViewPager::class.java.getDeclaredField("mScroller")
        scroller.isAccessible = true
        val interpolator = AccelerateDecelerateInterpolator()
        val fixedSpeedScroller = FixedSpeedScroller(context, interpolator)
        fixedSpeedScroller.setFixedDuration(pagerScrollDuration)
        scroller.set(viewPager, fixedSpeedScroller)
    }

    fun startLoop() {
        if (loop && !mBannerList.isEmpty()) mHandler.postDelayed(mLoopRunnable, duration.toLong())
    }

    fun stopLoop() {
        if (loop) mHandler.removeCallbacks(mLoopRunnable)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> stopLoop()
            MotionEvent.ACTION_UP -> if (loop) startLoop()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopLoop()
    }

    fun setBannerData(bannerList: MutableList<HomeBannerData>) {
        if (bannerList.isEmpty()) {
            defaultImage?.visibility = View.VISIBLE
            return
        }
        defaultImage?.visibility = View.GONE

        // 测量好第一张图片的高度后，再更新adapter中的数据
        GlideApp.with(this).asBitmap().load(bannerList[0].imagePath).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val width = resource.width
                val height = resource.height
                val screenWidth = DensityUtil.getScreenWidth() - (viewPager!!.paddingLeft + viewPager!!.paddingRight)
                val resultHeight = height * screenWidth / width
                layoutParams.height = resultHeight + (viewPager!!.paddingTop + viewPager!!.paddingBottom)
                mBannerList.addAll(bannerList)
                mBannerAdapter?.setImageHeight(resultHeight)
                indicator?.setViewPager(viewPager!!, mBannerList.size)
                val item = mBannerList.size * BannerPagerAdapter.SIZE_MULTIPLE / 2 - ((mBannerList.size * BannerPagerAdapter.SIZE_MULTIPLE) % mBannerList.size)
                viewPager?.currentItem = item
                if (loop) startLoop()
            }
        })
    }

}
