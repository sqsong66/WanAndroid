package com.sqsong.wanandroid.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.chip.Chip
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder.LoadingState
import com.sqsong.wanandroid.data.HomeBannerData
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.view.BannerView
import com.sqsong.wanandroid.view.CheckableImageView
import com.sqsong.wanandroid.view.CircleTextView
import com.sqsong.wanandroid.view.LabelView

class HomeItemAdapter(context: Context,
                      private val dataList: List<HomeItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mInflater = LayoutInflater.from(context)

    @LoadingState
    private var mLoadingState: Int = 0
    private var mActionListener: HomeItemActionListener? = null
    private var mBannerList: MutableList<HomeBannerData>? = null

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> return Constants.ITEM_TYPE_HEADER
            dataList.size + 1 -> Constants.ITEM_TYPE_FOOTER
            else -> return Constants.ITEM_TYPE_CONTENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.ITEM_TYPE_HEADER -> HomeBannerViewHolder(mInflater.inflate(R.layout.item_banner_header, parent, false))
            Constants.ITEM_TYPE_FOOTER -> LoadingFooterViewHolder(mInflater.inflate(R.layout.item_loading_footer, parent, false))
            else -> HomeItemViewHolder(mInflater.inflate(R.layout.item_home_news, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HomeBannerViewHolder -> {
                holder.bindBannerData(mBannerList)
            }
            is HomeItemViewHolder -> {
                holder.bindItemData(dataList[position - 1], position, mActionListener)
            }
            is LoadingFooterViewHolder -> {
                holder.updateLoadingState(mLoadingState)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size// + 2
    }

    fun setHomeItemActionListener(listener: HomeItemActionListener) {
        this.mActionListener = listener
    }

    fun setBannerList(bannerList: MutableList<HomeBannerData>) {
        this.mBannerList = bannerList
        notifyItemChanged(0)
    }

    fun updateLoadingState(@LoadingState state: Int) {
        this.mLoadingState = state
        notifyItemChanged(dataList.size + 1)
    }

    class HomeBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.bannerView)
        @JvmField
        var bannerView: BannerView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindBannerData(bannerList: MutableList<HomeBannerData>?) {
            if (bannerList != null) {
                bannerView?.setBannerData(bannerList)
            }
        }
    }

    class HomeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        }
    }

    interface HomeItemActionListener {
        fun onStarClick(homeItem: HomeItem, position: Int)
        fun onListItemClick(homeItem: HomeItem, position: Int)
    }

}