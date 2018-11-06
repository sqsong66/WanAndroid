package com.sqsong.wanandroid.view.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.sqsong.wanandroid.R

class MaterialSearchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var searchLayout: FrameLayout? = null
    private var searchBar: LinearLayout? = null
    private var backIv: ImageView? = null
    private var searchEdit: EditText? = null
    private var clearIv: ImageView? = null
    private var hotSearchLl: LinearLayout? = null
    private var hotSearchChipGroup: ChipGroup? = null
    private var searchHistoryLl: LinearLayout? = null
    private var clearHistoryTv: TextView? = null
    private var historyRecycler: RecyclerView? = null
    private var bgView: View? = null

    private var isSearchOpen = false

    init {
        inflateView()
        initAttrs(context, attrs, defStyleAttr)
    }

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.layout_search_view, this, true)
        searchLayout = findViewById(R.id.search_layout)
        searchBar = findViewById(R.id.search_bar)
        backIv = findViewById(R.id.back_iv)
        searchEdit = findViewById(R.id.search_edit)
        clearIv = findViewById(R.id.clear_iv)
        hotSearchLl = findViewById(R.id.hot_search_ll)
        hotSearchChipGroup = findViewById(R.id.hot_search_chip_group)
        searchHistoryLl = findViewById(R.id.search_history_ll)
        clearHistoryTv = findViewById(R.id.clear_history_tv)
        historyRecycler = findViewById(R.id.recycler)
        bgView = findViewById(R.id.bg_view)

        backIv?.setOnClickListener(this)
        bgView?.setOnClickListener(this)
        clearIv?.setOnClickListener(this)

    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialSearchView, defStyleAttr, 0)

        typedArray.recycle()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_iv, R.id.bg_view -> closeSearchView()
            R.id.clear_iv -> searchEdit?.text = null
        }
    }

    private fun closeSearchView() {
        if (!isSearchOpen) return

        searchEdit?.text = null
        clearFocus()
        isSearchOpen = false
    }

}