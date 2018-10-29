package com.sqsong.wanandroid.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.KnowledgeData

class ProjectPopAdapter(context: Context, private val dataList: List<KnowledgeData>) : RecyclerView.Adapter<ProjectPopAdapter.PopViewHolder>() {

    private val mInflater = LayoutInflater.from(context)
    private var mListener: OnItemClickListener<KnowledgeData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopViewHolder {
        return PopViewHolder(mInflater.inflate(R.layout.layout_project_pop, parent, false))
    }

    override fun onBindViewHolder(holder: PopViewHolder, position: Int) {
        holder.bindData(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener<KnowledgeData>) {
        this.mListener = listener
    }

    inner class PopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @JvmField
        @BindView(R.id.titleTv)
        var titleTv: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(knowledgeData: KnowledgeData, position: Int) {
            titleTv?.text = knowledgeData.name
            itemView.setOnClickListener {
                mListener?.onItemClick(knowledgeData, position)
            }
        }
    }

}