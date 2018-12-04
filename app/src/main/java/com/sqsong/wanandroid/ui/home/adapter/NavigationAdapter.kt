package com.sqsong.wanandroid.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.NavigationData
import com.sqsong.wanandroid.util.DensityUtil

class NavigationAdapter(val context: Context?, private val dataList: MutableList<NavigationData>) : RecyclerView.Adapter<NavigationAdapter.NavigationViewHolder>() {

    private val mInflater = LayoutInflater.from(context)
    private var mListener: OnItemClickListener<HomeItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {
        return NavigationViewHolder(mInflater.inflate(R.layout.item_navigation, parent, false))
    }

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        holder.bindData(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener<HomeItem>) {
        this.mListener = listener
    }

    inner class NavigationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.chipGroup)
        @JvmField
        var chipGroup: ChipGroup? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(navigationData: NavigationData, position: Int) {
            val articles = navigationData.articles
            chipGroup?.removeAllViews()
            if (!articles.isEmpty()) {
                articles.forEach {
                    val chip: Chip = createChip(it)
                    val data = it
                    chip.setOnClickListener { mListener?.onItemClick(data, position) }
                    chipGroup?.addView(chip)
                }
            }

            val layoutParams = chipGroup?.layoutParams as RecyclerView.LayoutParams
            if (position == dataList.size - 1) {
                layoutParams.bottomMargin = DensityUtil.dip2px(20)
            } else {
                layoutParams.bottomMargin = DensityUtil.dip2px(10)
            }
        }

        private fun createChip(homeItem: HomeItem): Chip {
            val chip = mInflater.inflate(R.layout.layout_knowledge_chip, chipGroup, false) as Chip
            chip.text = homeItem.title
            return chip
        }

    }

}