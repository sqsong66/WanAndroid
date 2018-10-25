package com.sqsong.wanandroid.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.util.DensityUtil

class KnowledgeAdapter(val context: Context, val dataList: MutableList<KnowledgeData>) : RecyclerView.Adapter<KnowledgeAdapter.BodyViewHolder>() {

    private val mInflater = LayoutInflater.from(context)
    private var mListener: OnItemClickListener<KnowledgeData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeAdapter.BodyViewHolder {
        return BodyViewHolder(mInflater.inflate(R.layout.item_knowledge_body, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: KnowledgeAdapter.BodyViewHolder, position: Int) {
        val knowledgeData = dataList[position]
        holder.bindData(knowledgeData, position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener<KnowledgeData>) {
        this.mListener = listener
    }

    inner class BodyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.headTv)
        @JvmField
        var headTv: TextView? = null

        @BindView(R.id.chipGroup)
        @JvmField
        var chipGroup: ChipGroup? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(knowledgeData: KnowledgeData, position: Int) {
            headTv?.text = knowledgeData.name

            val children = knowledgeData.children
            chipGroup?.removeAllViews()
            if (!children.isEmpty()) {
                children.forEach {
                    val chip: Chip = createChip(it)
                    val data = it
                    chip.setOnClickListener { mListener?.onItemClick(data, position) }
                    chipGroup?.addView(chip)
                }
            }

            val layoutParams = chipGroup?.layoutParams as ConstraintLayout.LayoutParams
            if (position == dataList.size - 1) {
                layoutParams.bottomMargin = DensityUtil.dip2px(10)
            } else {
                layoutParams.bottomMargin = 0
            }
        }

        private fun createChip(knowledgeData: KnowledgeData): Chip {
            val chip = mInflater.inflate(R.layout.layout_knowledge_chip, chipGroup, false) as Chip
            chip.text = knowledgeData.name
            return chip
        }
    }

}