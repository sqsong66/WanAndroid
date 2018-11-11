package com.sqsong.wanandroid.view.search

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.HotSearchData
import com.sqsong.wanandroid.util.AnimationUtil
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.PreferenceHelper.set
import kotlinx.android.synthetic.main.layout_search_view.view.*

class MaterialSearchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, SearchHistoryAdapter.OnSearchItemClickListener {

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
    private var contentLl: LinearLayout? = null

    private var isClearFocus = false
    private var isSearchOpen = false
    private var mMenuItem: MenuItem? = null
    private var mPreferences: SharedPreferences? = null
    private var mActionListener: OnSearchActionListener? = null
    private lateinit var mHistoryAdapter: SearchHistoryAdapter
    private var mHistoryList = mutableListOf<String>()

    private val mAnimListener: AnimationUtil.AnimationListener by lazy {
        return@lazy object : AnimationUtil.AnimationListener {
            override fun onAnimationStart(view: View): Boolean {
                return false
            }

            override fun onAnimationEnd(view: View): Boolean {
                mActionListener?.onSearchViewVisible()
                return false
            }

            override fun onAnimationCancel(view: View): Boolean {
                return false
            }
        }
    }

    init {
        inflateView()
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
        contentLl = findViewById(R.id.content_ll)

        backIv?.setOnClickListener(this)
        bgView?.setOnClickListener(this)
        clearIv?.setOnClickListener(this)
        contentLl?.setOnClickListener(this)
        clearHistoryTv?.setOnClickListener(this)
        setupRecycler()

        searchEdit?.setOnEditorActionListener { _, _, _ ->
            run {
                val key = searchEdit?.text.toString()
                if (!TextUtils.isEmpty(key)) {
                    if (mHistoryList.contains(key)) {
                        mHistoryList.remove(key)
                    }
                    mHistoryList.add(0, key)
                    mPreferences?.set(Constants.SEARCH_HISTORY_KEY, CommonUtil.convertListToString(mHistoryList))
                    mActionListener?.onSearch(key)
                    closeSearchView()
                }
                true
            }
        }

        searchEdit?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onTextChanged(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        searchEdit?.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus) {
                    showKeyboard(searchEdit)
                }
            }
        }
        mPreferences = PreferenceHelper.defaultPrefs(context)
    }

    private fun setupRecycler() {
        mHistoryAdapter = SearchHistoryAdapter(context, mHistoryList)
        mHistoryAdapter.setOnSearchItemClickListener(this)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mHistoryAdapter
    }

    private fun showKeyboard(searchEdit: View?) {
        val inputManager = searchEdit?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(searchEdit, 0)
    }

    private fun hideKeyboard(view: View?) {
        val inputManager = searchEdit?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun onTextChanged(editable: Editable?) {
        val value = editable?.toString()
        if (TextUtils.isEmpty(value)) {
            clearIv?.visibility = View.GONE
        } else {
            clearIv?.visibility = View.VISIBLE
        }
        mActionListener?.onTextChanged(value)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_iv -> closeSearchView()
            R.id.bg_view -> closeSearchView()
            R.id.clear_iv -> searchEdit?.text = null
            R.id.content_ll -> hideKeyboard(searchEdit)
            R.id.clear_history_tv -> {
                mHistoryList.clear()
                mHistoryAdapter.notifyDataSetChanged()
                mPreferences?.set(Constants.SEARCH_HISTORY_KEY, "")
                searchHistoryLl?.visibility = View.GONE
            }
        }
    }

    private fun showSearchView() {
        showSearch(true)
    }

    private fun showSearch(animate: Boolean) {
        if (isSearchOpen) return

        searchEdit?.text = null
        searchEdit?.requestFocus()

        querySearchHistory()
        if (animate) {
            showVisibleWithAnim()
        } else {
            searchLayout?.visibility = VISIBLE
            mActionListener?.onSearchViewVisible()
        }
        isSearchOpen = true
    }

    private fun showVisibleWithAnim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchLayout?.visibility = View.VISIBLE
            AnimationUtil.revealIn(contentLl!!, mAnimListener)
        } else {
            AnimationUtil.fadeInView(contentLl!!, ANIMATION_DURATION, mAnimListener)
        }
    }

    private fun hideViewWithAnim() {
        hideKeyboard(searchEdit)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtil.revealOut(contentLl!!, object : AnimationUtil.AnimationListener {
                override fun onAnimationStart(view: View): Boolean {
                    return false
                }

                override fun onAnimationEnd(view: View): Boolean {
                    searchLayout?.visibility = View.GONE
                    isSearchOpen = false
                    return false
                }

                override fun onAnimationCancel(view: View): Boolean {
                    return false
                }
            })
        } else {
            searchLayout?.visibility = View.GONE
            mActionListener?.onSearchViewGone()
            isSearchOpen = false
        }
    }

    fun closeSearchView() {
        if (!isSearchOpen) return

        searchEdit?.text = null
        clearFocus()
        hideViewWithAnim()
    }

    fun isSearchViewShow(): Boolean = isSearchOpen

    fun setMenuItem(menuItem: MenuItem?) {
        this.mMenuItem = menuItem
        val actionView = menuItem?.actionView
        actionView?.x
        menuItem?.setOnMenuItemClickListener {
            showSearchView()
            return@setOnMenuItemClickListener true
        }
    }

    fun setOnSearchActionListener(listener: OnSearchActionListener) {
        this.mActionListener = listener
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (!isFocusable || isClearFocus) return false
        return searchEdit?.requestFocus(direction, previouslyFocusedRect) ?: false
    }

    override fun clearFocus() {
        isClearFocus = true
        hideKeyboard(this)
        super.clearFocus()
        searchEdit?.clearFocus()
        isClearFocus = false
    }

    fun setHotSearchData(keyList: List<HotSearchData>?) {
        if (keyList == null || keyList.isEmpty()) {
            hotSearchLl?.visibility = View.GONE
            return
        }
        hotSearchChipGroup?.removeAllViews()
        for (key in keyList) {
            val name = key.name
            val chip = createChip(name)
            chip.setOnClickListener {
                if (mHistoryList.contains(name)) {
                    mHistoryList.remove(name)
                }
                mHistoryList.add(0, name)
                mPreferences?.set(Constants.SEARCH_HISTORY_KEY, CommonUtil.convertListToString(mHistoryList))
                mActionListener?.onSearch(key.name)
                closeSearchView()
            }
            hotSearchChipGroup?.addView(chip)
        }
    }

    private fun createChip(text: String): Chip {
        val chip = Chip(context)
        val layoutParams = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        chip.layoutParams = layoutParams
        chip.text = text
        chip.setTextColor(ContextCompat.getColor(context, R.color.colorTextInActive))
        return chip
    }

    private fun querySearchHistory() {
        val keyStr: String? = mPreferences?.get(Constants.SEARCH_HISTORY_KEY)
        if (keyStr == null || TextUtils.isEmpty(keyStr)) {
            searchHistoryLl?.visibility = View.GONE
            return
        }
        searchHistoryLl?.visibility = View.VISIBLE
        val keyArray = keyStr.split(",")
        mHistoryList.clear()
        for (key in keyArray) {
            mHistoryList.add(key)
        }
        mHistoryAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(text: String, position: Int) {
        mHistoryList.remove(text)
        mHistoryList.add(0, text)
        mPreferences?.set(Constants.SEARCH_HISTORY_KEY, CommonUtil.convertListToString(mHistoryList))
        mActionListener?.onSearch(text)
        closeSearchView()
    }

    override fun onDeleteClick(text: String, position: Int) {
        mHistoryList.remove(text)
        if (mHistoryList.size == 0) {
            searchHistoryLl?.visibility = View.GONE
            mHistoryAdapter.notifyDataSetChanged()
            mPreferences?.set(Constants.SEARCH_HISTORY_KEY, "")
        } else {
            mHistoryAdapter.notifyItemRemoved(position)
            mPreferences?.set(Constants.SEARCH_HISTORY_KEY, CommonUtil.convertListToString(mHistoryList))
            postDelayed({ mHistoryAdapter.notifyDataSetChanged() }, 300)
        }
    }

    interface OnSearchActionListener {
        fun onSearch(key: String)
        fun onSearchViewVisible()
        fun onSearchViewGone()
        fun onTextChanged(text: String?)
    }

    companion object {
        const val ANIMATION_DURATION: Int = 400
    }
}
