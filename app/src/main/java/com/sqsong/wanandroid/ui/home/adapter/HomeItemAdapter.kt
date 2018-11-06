package com.sqsong.wanandroid.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
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
import com.sqsong.wanandroid.view.CheckableImageView
import com.sqsong.wanandroid.view.CircleTextView
import com.sqsong.wanandroid.view.LabelView

class HomeItemAdapter(context: Context,
                      private val dataList: List<HomeItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mInflater = LayoutInflater.from(context)

    @LoadingState
    private var mLoadingState: Int = 0
    private lateinit var mHeaderView: View
    private var mActionListener: HomeItemActionListener? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> return Constants.ITEM_TYPE_HEADER
            in 1..dataList.size -> Constants.ITEM_TYPE_CONTENT
            dataList.size + 1 -> Constants.ITEM_TYPE_FOOTER
            else -> return Constants.ITEM_TYPE_NONE
        }
    }

    fun setHeaderView(headerView: View) {
        this.mHeaderView = headerView
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.ITEM_TYPE_HEADER -> HomeBannerViewHolder(mHeaderView)
            Constants.ITEM_TYPE_FOOTER -> LoadingFooterViewHolder(mInflater.inflate(R.layout.item_loading_footer, parent, false))
            Constants.ITEM_TYPE_CONTENT -> HomeItemViewHolder(mInflater.inflate(R.layout.item_home_news, parent, false))
            else -> null!!
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HomeItemViewHolder -> holder.bindItemData(dataList[position - 1], position)
            is LoadingFooterViewHolder -> holder.updateLoadingState(mLoadingState)
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

        @BindView(R.id.titleTv)
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

        @BindView(R.id.starRl)
        @JvmField
        var starRl: RelativeLayout? = null

        @BindView(R.id.shareIv)
        @JvmField
        var shareIv: ImageView? = null

        init {
            ButterKnife.bind(this@HomeItemViewHolder, itemView)
        }

        fun bindItemData(homeItem: HomeItem, position: Int) {
            nameCircleTv?.setText(homeItem.author.trim().substring(0, 1))
            labelView?.visibility = if (homeItem.fresh) View.VISIBLE else View.INVISIBLE
            authorTv?.text = homeItem.author
            timeTv?.text = homeItem.niceDate
            titleTv?.text = homeItem.title
            superChapterChip?.text = homeItem.superChapterName
            chapterChip?.text = homeItem.chapterName
            starIv?.isChecked = homeItem.collect

            shareIv?.setOnClickListener {
                mActionListener?.onShareClick(homeItem, position)
            }

            starRl?.setOnClickListener {
                mActionListener?.onStarClick(homeItem, position)
            }

            itemView.setOnClickListener {
                mActionListener?.onListItemClick(homeItem, position)
            }
        }
    }

    interface HomeItemActionListener {
        fun onStarClick(homeItem: HomeItem, position: Int)
        fun onListItemClick(homeItem: HomeItem, position: Int)
        fun onShareClick(homeItem: HomeItem, position: Int)
    }

}