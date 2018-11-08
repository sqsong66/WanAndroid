package com.sqsong.wanandroid.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.sqsong.wanandroid.R

class SearchHistoryAdapter(private val context: Context,
                           private val dataList: List<String>) : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {

    private var mListener: OnSearchItemClickListener? = null
    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(mInflater.inflate(R.layout.item_search_history, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindData(dataList[position], position)
    }

    fun setOnSearchItemClickListener(listener: OnSearchItemClickListener) {
        this.mListener = listener
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.searchText)
        @JvmField
        var searchText: TextView? = null

        @BindView(R.id.deleteIv)
        @JvmField
        var deleteIv: ImageView? = null

        @BindView(R.id.line)
        @JvmField
        var line: View? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(text: String, position: Int) {
            searchText?.text = text
            itemView.setOnClickListener {
                mListener?.onItemClick(text, position)
            }
            deleteIv?.setOnClickListener {
                mListener?.onDeleteClick(text, position)
            }
            if (position == dataList.size - 1) {
                line?.visibility = View.GONE
            } else {
                line?.visibility = View.VISIBLE
            }
        }
    }

    interface OnSearchItemClickListener {
        fun onItemClick(text: String, position: Int)
        fun onDeleteClick(text: String, position: Int)
    }

}