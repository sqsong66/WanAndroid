package com.sqsong.wanandroid.ui.home.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.chip.Chip
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder.LoadingState
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.animator.ViewHelper
import com.sqsong.wanandroid.view.CheckableImageView
import com.sqsong.wanandroid.view.CircleTextView
import com.sqsong.wanandroid.view.LabelView

class HomeItemAdapter(context: Context,
                      private val dataList: List<HomeItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mInflater = LayoutInflater.from(context)

    var isFirstOnly = true
    var mLastPosition: Int = 0
    val mInterpolator = LinearInterpolator()

    @LoadingState
    private var mLoadingState: Int = 0
    private lateinit var mHeaderView: View
    private var mActionListener: HomeItemActionListener? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> return Constants.ITEM_TYPE_HEADER
            dataList.size + 1 -> Constants.ITEM_TYPE_FOOTER
            else -> return Constants.ITEM_TYPE_CONTENT
        }
    }

    fun setHeaderView(headerView: View) {
        this.mHeaderView = headerView
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.ITEM_TYPE_HEADER -> {
                val holder = HomeBannerViewHolder(mHeaderView)
                holder.setIsRecyclable(false)
                return holder
            }
            Constants.ITEM_TYPE_FOOTER -> LoadingFooterViewHolder(mInflater.inflate(R.layout.item_loading_footer, parent, false))
            else -> HomeItemViewHolder(mInflater.inflate(R.layout.item_home_news, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Constants.ITEM_TYPE_HEADER -> {
            }
            Constants.ITEM_TYPE_CONTENT -> {
                (holder as HomeItemViewHolder).bindItemData(dataList[position - 1], position, mActionListener)
            }
            Constants.ITEM_TYPE_FOOTER -> {
                (holder as LoadingFooterViewHolder).updateLoadingState(mLoadingState)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size + 2
    }

    fun setHomeItemActionListener(listener: HomeItemActionListener) {
        this.mActionListener = listener
    }


    fun updateLoadingState(@LoadingState state: Int) {
        this.mLoadingState = state
        notifyItemChanged(dataList.size + 1)
    }

    fun resetAnimationPosition() {
        mLastPosition = 0
    }

    class HomeBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class HomeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.nameCircleTv)
        @JvmField
        var nameCircleTv: CircleTextView? = null

        @BindView(R.id.labelView)
        @JvmField
        var labelView: LabelView? = null

        @BindView(R.id.authorTv)
        @JvmField
        var authorTv: TextView? = null

        @BindView(R.id.timeTv)
        @JvmField
        var timeTv: TextView? = null

        @BindView(R.id.loginTv)
        @JvmField
        var titleTv: TextView? = null

        @BindView(R.id.superChapterChip)
        @JvmField
        var superChapterChip: Chip? = null

        @BindView(R.id.chapterChip)
        @JvmField
        var chapterChip: Chip? = null

        @BindView(R.id.starIv)
        @JvmField
        var starIv: CheckableImageView? = null

        init {
            ButterKnife.bind(this@HomeItemViewHolder, itemView)
        }

        fun bindItemData(homeItem: HomeItem, position: Int, listener: HomeItemActionListener?) {
            nameCircleTv?.setText(homeItem.author.substring(0, 1))
            labelView?.visibility = if (homeItem.fresh) View.VISIBLE else View.INVISIBLE
            authorTv?.text = homeItem.author
            timeTv?.text = homeItem.niceDate
            titleTv?.text = homeItem.title
            superChapterChip?.text = homeItem.superChapterName
            chapterChip?.text = homeItem.chapterName
            starIv?.isChecked = homeItem.zan != 0

            starIv?.setOnClickListener {
                listener?.onStarClick(homeItem, position)
            }

            itemView.setOnClickListener {
                listener?.onListItemClick(homeItem, position)
            }

            if (!isFirstOnly || position - 1 > mLastPosition) {
                mLastPosition = position - 1
                val scaleX = ObjectAnimator.ofFloat(itemView, "scaleX", 0.5f, 1f)
                val scaleY = ObjectAnimator.ofFloat(itemView, "scaleY", 0.5f, 1f)
                val animatorSet = AnimatorSet()
                animatorSet.interpolator = mInterpolator
                animatorSet.duration = 300
                animatorSet.playTogether(scaleX, scaleY)
                animatorSet.start()
            } else {
                ViewHelper.clear(itemView)
            }
        }
    }

    interface HomeItemActionListener {
        fun onStarClick(homeItem: HomeItem, position: Int)
        fun onListItemClick(homeItem: HomeItem, position: Int)
    }

}