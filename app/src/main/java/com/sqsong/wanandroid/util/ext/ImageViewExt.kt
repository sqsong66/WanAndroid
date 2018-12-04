package com.sqsong.wanandroid.util.ext

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.GlideApp

fun ImageView.showImage(context: Context?, url: String) {
    showImage(context, url, 0, false, 0)
}

fun ImageView.showImage(context: Context?, url: String, placeholder: Int) {
    showImage(context, url, placeholder, false, 0)
}

fun ImageView.showImage(context: Context?, resId: Int) {
    showImage(context, resId, 0, false, 0)
}

fun ImageView.showImage(context: Context, resId: Int, placeholder: Int) {
    showImage(context, resId, placeholder, false, 0)
}

fun ImageView.showRoundCornerImage(context: Context?, url: String) {
    showImage(context, url, 0, true, dpToPx(5))
}

fun ImageView.showRoundCornerImage(context: Context?, url: String, radius: Int) {
    showImage(context, url, 0, true, radius)
}

fun ImageView.showRoundCornerImage(context: Context, url: String, placeholder: Int, radius: Int) {
    showImage(context, url, placeholder, true, radius)
}

fun ImageView.showRoundCornerImage(context: Context?, resId: Int) {
    showImage(context, resId, 0, true, dpToPx(5))
}


fun ImageView.showRoundCornerImage(context: Context?, resId: Int, radius: Int) {
    showImage(context, resId, 0, true, radius)
}

fun ImageView.showRoundCornerImage(context: Context?, resId: Int, placeholder: Int, radius: Int) {
    showImage(context, resId, placeholder, true, radius)
}

fun ImageView.showCircleImage(context: Context?, url: String) {
    showImage(context, url, 0, true, false, 0)
}

fun ImageView.showCircleImage(context: Context?, url: String, placeholder: Int) {
    showImage(context, url, placeholder, true, false, 0)
}

fun ImageView.showCircleImage(context: Context?, resId: Int) {
    showImage(context, resId, 0, true, false, 0)
}

fun ImageView.showCircleImage(context: Context?, resId: Int, placeholder: Int) {
    showImage(context, resId, placeholder, true, false, 0)
}

private fun ImageView.showImage(context: Context?, url: String, placeholder: Int, roundCorner: Boolean, radius: Int) {
    showImage(context, url, placeholder, false, roundCorner, radius)
}

private fun ImageView.showImage(context: Context?, resId: Int, placeholder: Int, roundCorner: Boolean, radius: Int) {
    showImage(context, resId, placeholder, false, roundCorner, radius)
}

fun ImageView.showImage(context: Context?, url: String, placeholder: Int, circle: Boolean, roundCorner: Boolean, radius: Int) {
    if (context == null) return
    var holder = placeholder
    if (holder == 0) {
        holder = R.drawable.image_placeholder
    }
    val glide = GlideApp.with(context)
            .load(url)
            .placeholder(holder)
            .centerCrop()
            .transition(withCrossFade())
    if (roundCorner) {
        var transformation = MultiTransformation(CenterCrop(), RoundedCorners(radius))
        glide.apply(bitmapTransform(transformation))
    } else if (circle) {
        glide.apply(RequestOptions().circleCrop())
    }
    glide.into(this)
}

fun ImageView.showImage(context: Context?, resId: Int, placeholder: Int, circle: Boolean, roundCorner: Boolean, radius: Int) {
    if (context == null) return
    var holder = placeholder
    if (holder == 0) {
        holder = R.drawable.image_placeholder
    }
    val glide = GlideApp.with(context)
            .load(resId)
            .placeholder(holder)
            .transition(withCrossFade())
    if (roundCorner) {
        var transformation = MultiTransformation(CenterCrop(), RoundedCorners(radius))
        glide.apply(bitmapTransform(transformation))
    } else if (circle) {
        glide.apply(RequestOptions().circleCrop())
    }
    glide.into(this)
}

fun ImageView.dpToPx(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}