package com.sqsong.wanandroid.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.HomeBannerData
import com.sqsong.wanandroid.common.GlideApp
import com.sqsong.wanandroid.util.DensityUtil

class BannerPagerAdapter(private val context: Context,
                         private val viewPager: ViewPager,
                         private val bannerList: MutableList<HomeBannerData>) : PagerAdapter() {

    private var mImageWidth: Int? = 0
    private var mImageHeight: Int? = 0
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    fun setImageSize(imageWidth: Int, imageHeight: Int) {
        this.mImageWidth = imageWidth
        this.mImageHeight = imageHeight
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mInflater.inflate(R.layout.layout_home_banner, null)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(DensityUtil.dip2px(5)))

        val bannerData = bannerList[position % bannerList.size]
        GlideApp.with(context)
                .load(bannerData.imagePath)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.bitmapTransform(transformation))
                .into(imageView)
        container.addView(view)
        view.layoutParams.height = mImageHeight!!
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return bannerList.size * SIZE_MULTIPLE
    }

    override fun finishUpdate(container: ViewGroup) {
        var currentItem = viewPager.currentItem
        if (currentItem == count) {
            currentItem = 0
            viewPager.setCurrentItem(currentItem, false)
        }
    }

    companion object {
        const val SIZE_MULTIPLE = 500
    }
}