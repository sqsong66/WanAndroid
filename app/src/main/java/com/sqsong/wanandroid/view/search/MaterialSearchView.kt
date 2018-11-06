package com.sqsong.wanandroid.view.search

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.util.AnimationUtil

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
    private var contentLl: LinearLayout? = null

    private var isClearFocus = false
    private var isSearchOpen = false
    private var mMenuItem: MenuItem? = null
    private var mActionListener: OnSearchActionListener? = null

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
        // initAttrs(context, attrs, defStyleAttr)
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

        searchEdit?.setOnEditorActionListener { _, _, _ ->
            run {
                val key = searchEdit?.text.toString()
                if (!TextUtils.isEmpty(key)) {
                    mActionListener?.onSearch(key)
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
    }

    private fun showKeyboard(searchEdit: View?) {
//        if (searchEdit?.hasFocus() == true) searchEdit.clearFocus()
//        searchEdit?.requestFocus()
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
        }
    }

    private fun showSearchView() {
        showSearch(true)
    }

    private fun showSearch(animate: Boolean) {
        if (isSearchOpen) return

        searchEdit?.text = null
        searchEdit?.requestFocus()

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
            AnimationUtil.reveal(contentLl!!, mAnimListener)
        } else {
            AnimationUtil.fadeInView(contentLl!!, ANIMATION_DURATION, mAnimListener)
        }
    }

    private fun closeSearchView() {
        if (!isSearchOpen) return

        searchEdit?.text = null
        clearFocus()

        searchLayout?.visibility = View.GONE
        mActionListener?.onSearchViewGone()
        isSearchOpen = false
    }

    fun setMenuItem(menuItem: MenuItem?) {
        this.mMenuItem = menuItem
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