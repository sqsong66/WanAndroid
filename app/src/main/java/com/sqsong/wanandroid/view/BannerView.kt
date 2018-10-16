package com.sqsong.wanandroid.view

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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
import com.sqsong.wanandroid.common.GlideApp
import com.sqsong.wanandroid.data.HomeBannerData
import com.sqsong.wanandroid.ui.home.adapter.BannerPagerAdapter
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
    private var mBannerList = mutableListOf<HomeBannerData>()
    private val mBannerAdapter by lazy {
        BannerPagerAdapter(context, viewPager!!, mBannerList)
    }

    private val mLoopRunnable = object : Runnable {
        override fun run() {
            val currentItem: Int = viewPager?.currentItem!!
            if (currentItem == (viewPager?.adapter?.count?.minus(1))) {
                viewPager?.currentItem = 0
            } else {
                viewPager?.currentItem = currentItem + 1
            }
            mHandler.postDelayed(this, duration.toLong())
        }
    }

    init {
        initParams(context, attrs)
        initView(context)
    }

    private fun initParams(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView)
        loop = typedArray.getBoolean(R.styleable.BannerView_loop, true)
        duration = typedArray.getInteger(R.styleable.BannerView_duration, 5000)
        showIndicator = typedArray.getBoolean(R.styleable.BannerView_showIndicator, true)
        pagerScrollDuration = typedArray.getInteger(R.styleable.BannerView_pagerScrollDuration, 500)
        typedArray.recycle()
    }

    private fun initView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_banner, this, false)
        addView(view)
        val viewParams = view.layoutParams

        viewPager = view.findViewById(R.id.viewPager)
        indicator = view.findViewById(R.id.indicator)
        defaultImage = view.findViewById(R.id.default_iv)

        setViewPagerScrollDuration()
        viewPager?.setPageTransformer(true, ZoomPageTransformer(viewPager!!)
                /*AlphaAndScalePageTransformer()*/)
        viewPager?.pageMargin = DensityUtil.dip2px(10)

        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(DensityUtil.dip2px(5)))
        GlideApp.with(this)
                .load(R.drawable.placeholder)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(transformation))
                .into(defaultImage!!)
        // 根据图片高度来计算banner高度
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
                        viewParams.height = resultHeight + params.topMargin + params.bottomMargin
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

        // 测量好第一张图片的高度后，再更新adapter中的数据
        GlideApp.with(this).asBitmap().load(bannerList[0].imagePath).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val screenWidth = DensityUtil.getScreenWidth() - (viewPager!!.paddingLeft + viewPager!!.paddingRight)
                val resultHeight = resource.height * screenWidth / resource.width
                setupPagerAdapter(bannerList, screenWidth, resultHeight)
            }
        })
    }

    private fun setupPagerAdapter(bannerList: MutableList<HomeBannerData>, screenWidth: Int, resultHeight: Int) {
        defaultImage?.visibility = View.GONE
        mBannerList.clear()
        mBannerList.addAll(bannerList)
        viewPager?.adapter = mBannerAdapter
        mBannerAdapter.setImageSize(screenWidth, resultHeight)

        val item = mBannerList.size * BannerPagerAdapter.SIZE_MULTIPLE / 2 - ((mBannerList.size * BannerPagerAdapter.SIZE_MULTIPLE) % mBannerList.size)
        viewPager?.currentItem = item
        indicator?.setViewPager(viewPager!!, mBannerList.size)
        if (showIndicator) {
            indicator?.visibility = View.VISIBLE
        } else {
            indicator?.visibility = View.GONE
        }
        if (loop) startLoop()
    }

}
