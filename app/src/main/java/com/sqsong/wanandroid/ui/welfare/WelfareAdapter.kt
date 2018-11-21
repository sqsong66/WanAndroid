package com.sqsong.wanandroid.ui.welfare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder.LoadingState
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.WelfareData
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.ext.showImage

class WelfareAdapter constructor(private val context: Context, private val dataList: List<WelfareData>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @LoadingState
    private var mLoadingState: Int = 0
    private var mImageHeight: Int = 0
    private val mInflater = LayoutInflater.from(context)
    private var mListener: OnItemClickListener<WelfareData>? = null

    init {
        val spacing = context.resources.getDimensionPixelSize(R.dimen.picture_grid_spacing)
        val imageWidth = (DensityUtil.getScreenWidth() - spacing * 3) / 2
        mImageHeight = imageWidth * 4 / 3
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size) Constants.ITEM_TYPE_FOOTER else Constants.ITEM_TYPE_CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.ITEM_TYPE_CONTENT) {
            WelfareViewHolder(mInflater.inflate(R.layout.item_welfare, parent, false))
        } else {
            LoadingFooterViewHolder(mInflater.inflate(R.layout.item_loading_footer, parent, false))
        }
    }

    override fun getItemCount(): Int = dataList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WelfareViewHolder) {
            holder.bindData(dataList[position], position)
        } else if (holder is LoadingFooterViewHolder) {
            holder.updateLoadingState(mLoadingState)
        }
    }

    fun updateLoadingState(@LoadingState state: Int) {
        this.mLoadingState = state
        notifyItemChanged(dataList.size)
    }

    fun setOnItemClickListener(listener: OnItemClickListener<WelfareData>) {
        this.mListener = listener
    }

    inner class WelfareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.image)
        @JvmField
        var image: ImageView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(welfareData: WelfareData, position: Int) {
            image?.showImage(context, welfareData.url)
            image?.layoutParams?.height = mImageHeight

            itemView.setOnClickListener {
                mListener?.onItemClick(welfareData, position)
            }
        }

    }
}