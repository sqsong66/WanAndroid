package com.sqsong.wanandroid.common.holder

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.sqsong.wanandroid.R

class LoadingFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.progress)
    @JvmField
    var progressBar: ProgressBar? = null

    @BindView(R.id.loadingTv)
    @JvmField
    var loadingTv: TextView? = null

    init {
        // item_loading_footer
        ButterKnife.bind(this, itemView)
    }

    fun updateLoadingState(@LoadingState state: Int) {
        when (state) {
            STATE_LOADING -> {
                itemView.visibility = View.VISIBLE
                progressBar?.visibility = View.VISIBLE
                loadingTv?.visibility = View.VISIBLE
                loadingTv?.setText(R.string.text_loading)
            }
            STATE_NO_CONTENT -> {
                itemView.visibility = View.VISIBLE
                progressBar?.visibility = View.GONE
                loadingTv?.visibility = View.VISIBLE
                loadingTv?.setText(R.string.text_no_data)
            }
            STATE_HIDDEN -> {
                itemView.visibility = View.GONE
            }
        }
    }

    companion object {
        const val STATE_LOADING = 0
        const val STATE_NO_CONTENT = 1
        const val STATE_HIDDEN = 2
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STATE_LOADING, STATE_NO_CONTENT, STATE_HIDDEN)
    annotation class LoadingState

}