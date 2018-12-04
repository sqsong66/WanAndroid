package com.sqsong.wanandroid.ui.collection.adapter

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
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder.LoadingState
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.view.CheckableImageView
import com.sqsong.wanandroid.view.LabelView
import javax.inject.Inject

class CollectionAdapter @Inject constructor(context: Context?, private val dataList: MutableList<HomeItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @LoadingState
    private var mLoadingState: Int = 0
    private val mInflater = LayoutInflater.from(context)
    private var mActionListener: HomeItemAdapter.HomeItemActionListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size) Constants.ITEM_TYPE_FOOTER else Constants.ITEM_TYPE_CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.ITEM_TYPE_CONTENT) {
            KnowledgeViewHolder(mInflater.inflate(R.layout.item_knowledge, parent, false))
        } else {
            LoadingFooterViewHolder(mInflater.inflate(R.layout.item_loading_footer, parent, false))
        }
    }

    override fun getItemCount(): Int = dataList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is KnowledgeViewHolder) {
            holder.bindData(dataList[position], position)
        } else if (holder is LoadingFooterViewHolder) {
            holder.updateLoadingState(mLoadingState)
        }
    }

    fun updateLoadingState(@LoadingState state: Int) {
        this.mLoadingState = state
        notifyItemChanged(dataList.size)
    }

    fun setHomeItemActionListener(listener: HomeItemAdapter.HomeItemActionListener) {
        this.mActionListener = listener
    }

    inner class KnowledgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.labelView)
        @JvmField
        var labelView: LabelView? = null

        @BindView(R.id.titleTv)
        @JvmField
        var titleTv: TextView? = null

        @BindView(R.id.authorTv)
        @JvmField
        var authorTv: TextView? = null

        @BindView(R.id.timeTv)
        @JvmField
        var timeTv: TextView? = null

        @BindView(R.id.heartRl)
        @JvmField
        var heartRl: RelativeLayout? = null

        @BindView(R.id.heartIv)
        @JvmField
        var heartIv: CheckableImageView? = null

        @BindView(R.id.shareIv)
        @JvmField
        var shareIv: ImageView? = null

        @BindView(R.id.line)
        @JvmField
        var line: View? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(homeItem: HomeItem, position: Int) {
            labelView?.visibility = if (homeItem.fresh) View.VISIBLE else View.INVISIBLE
            authorTv?.text = homeItem.author
            timeTv?.text = homeItem.niceDate
            titleTv?.text = homeItem.title
            heartIv?.isChecked = true //homeItem.collect

            heartRl?.setOnClickListener {
                mActionListener?.onStarClick(homeItem, position)
            }

            shareIv?.setOnClickListener {
                mActionListener?.onShareClick(homeItem, position)
            }

            itemView.setOnClickListener {
                mActionListener?.onListItemClick(homeItem, position)
            }

            val params = itemView.layoutParams as RecyclerView.LayoutParams
            if (position == dataList.size - 1) {
                params.bottomMargin = DensityUtil.dip2px(16)
            } else {
                params.bottomMargin = 0
            }

            if (position == dataList.size - 1) {
                line?.visibility = View.GONE
            } else {
                line?.visibility = View.VISIBLE
            }

            /*titleTv?.text = homeItem.title
            authorTv?.text = homeItem.author
            timeTv?.text = homeItem.niceDate
            heartIv?.isChecked = homeItem.collect
            labelView?.visibility = if (homeItem.fresh) View.VISIBLE else View.INVISIBLE

            heartRl?.setOnClickListener {
                mActionListener?.onStarClick(homeItem, position)
            }

            itemView.setOnClickListener {
                mActionListener?.onListItemClick(homeItem, position)
            }

            if (position == dataList.size - 1) {
                line?.visibility = View.GONE
            } else {
                line?.visibility = View.VISIBLE
            }*/
        }

    }

}