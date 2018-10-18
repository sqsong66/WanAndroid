package com.sqsong.wanandroid.view.banner

import android.content.Context
import android.graphics.Bitmap
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
import com.sqsong.wanandroid.view.CirclePagerIndicator
import com.sqsong.wanandroid.view.FixedSpeedScroller

class BannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    private var duration: Int = 0
    private var loop: Boolean = false
    private var pagerScrollDuration: Int = 0
    private var showIndicator: Boolean = false

    private lateinit var viewPager: ViewPager
    private lateinit var defaultImage: ImageView
    private lateinit var indicator: CirclePagerIndicator

    private var mDownX: Float = 0f
    private var mDownY: Float = 0f
    private var mLoopHandler: LoopHandler? = null
    private var mBannerList = mutableListOf<HomeBannerData>()
    private val mBannerAdapter by lazy {
        BannerPagerAdapter(context, viewPager, mBannerList)
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

        mLoopHandler = LoopHandler(this)
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
        viewPager.setPageTransformer(true, ZoomPageTransformer(viewPager)
                /*AlphaAndScalePageTransformer()*/)
        viewPager.pageMargin = DensityUtil.dip2px(10)

        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(DensityUtil.dip2px(5)))
        GlideApp.with(this)
                .load(R.drawable.placeholder)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(transformation))
                .into(defaultImage)
        // 根据图片高度来计算banner高度
        GlideApp.with(this)
                .asBitmap()
                .load(R.drawable.placeholder)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val width = resource.width
                        val height = resource.height
                        val params = defaultImage.layoutParams as ConstraintLayout.LayoutParams
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

    fun getViewPager(): ViewPager {
        return viewPager
    }

    fun isLoop(): Boolean {
        return loop
    }

    fun sendScrollMessage() {
        removeAllMessage()
        mLoopHandler?.sendEmptyMessageDelayed(0, duration.toLong())
    }

    private fun startAutoLoop() {
        if (!loop || mBannerList.isEmpty()) return
        removeAllMessage()
        mLoopHandler?.sendEmptyMessage(1)
    }

    private fun stopAutoLoop() {
        removeAllMessage()
    }

    private fun removeAllMessage() {
        mLoopHandler?.removeMessages(0)
        mLoopHandler?.removeMessages(1)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.VISIBLE) {
            startAutoLoop()
        } else {
            if (loop) stopAutoLoop()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 当Y方向滑动距离大于X方向滑动距离时不获取滚动事件
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.x
                mDownY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (Math.abs(ev.y - mDownY) > Math.abs(ev.x - mDownX)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                mDownX = ev.x
                mDownY = ev.y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun setBannerData(bannerList: MutableList<HomeBannerData>) {
        if (bannerList.isEmpty()) {
            defaultImage.visibility = View.VISIBLE
            return
        }

        // 测量好第一张图片的高度后，再更新adapter中的数据
        GlideApp.with(this).asBitmap().load(bannerList[0].imagePath).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val screenWidth = DensityUtil.getScreenWidth() - (viewPager.paddingLeft + viewPager.paddingRight)
                val resultHeight = resource.height * screenWidth / resource.width
                setupPagerAdapter(bannerList, screenWidth, resultHeight)
            }
        })
    }

    private fun setupPagerAdapter(bannerList: MutableList<HomeBannerData>, screenWidth: Int, resultHeight: Int) {
        defaultImage.visibility = View.GONE
        mBannerList.clear()
        mBannerList.addAll(bannerList)
        viewPager.adapter = mBannerAdapter
        mBannerAdapter.setImageSize(screenWidth, resultHeight)

        val item = mBannerList.size * BannerPagerAdapter.SIZE_MULTIPLE / 2 - ((mBannerList.size * BannerPagerAdapter.SIZE_MULTIPLE) % mBannerList.size)
        viewPager.setCurrentItem(item, true)
        indicator.setViewPager(viewPager, mBannerList.size)
        if (showIndicator) {
            indicator.visibility = View.VISIBLE
        } else {
            indicator.visibility = View.GONE
        }
        if (loop) startAutoLoop()
    }

}
