package com.sqsong.wanandroid.util.recycler

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.wanandroid.util.DensityUtil

class FloatingTitleItemDecoration(val builder: Builder) : RecyclerView.ItemDecoration() {

    private var mTextHeight: Float = .0f
    private var mTitleHeight: Float = .0f
    private var mTextBaseLine: Float = .0f
    private lateinit var mBgPaint: Paint
    private var mRectBounds: Rect = Rect()
    private lateinit var mTextPaint: Paint
    private lateinit var mContext: Context
    private lateinit var mShadowPaint: Paint
    private lateinit var mTitleMap: Map<Int, String>

    init {
        initParams()
    }

    private fun initParams() {
        if (builder.context == null) throw IllegalArgumentException("The context cannot be null.")
        if (builder.titleMap == null) throw IllegalArgumentException("The title map cannot be null.")
        mContext = builder.context
        mTitleMap = builder.titleMap!!
        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mShadowPaint.color = builder.backgroundColor ?: Color.WHITE
        mShadowPaint.setShadowLayer(10f, .0f, .0f, builder.backgroundColor ?: Color.WHITE)
        builder.recyclerView?.setLayerType(View.LAYER_TYPE_SOFTWARE, mShadowPaint)

        mBgPaint.color = builder.backgroundColor ?: Color.WHITE
        mTextPaint.color = builder.textColor ?: Color.BLACK
        mTextPaint.textSize = dip2px(builder.textSize ?: 16).toFloat()
        mTextPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val fontMetrics = mTextPaint.fontMetrics
        mTextHeight = fontMetrics.bottom - fontMetrics.top
        mTitleHeight = mTextHeight + builder.topPadding + builder.bottomPadding
        mTextBaseLine = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        val top: Int = if (mTitleMap.contains(position)) mTitleHeight.toInt() else 0
        outRect.set(0, top, 0, 0)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = (child.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
            if (mTitleMap.containsKey(position)) {
                drawTitle(c, parent, child, position)
            }
        }
    }

    private fun drawTitle(canvas: Canvas, recycler: RecyclerView, child: View, position: Int) {
        val left = .0f
        val right = recycler.width.toFloat()
        recycler.getDecoratedBoundsWithMargins(child, mRectBounds)
        val top = mRectBounds.top.toFloat()
        val bottom = top + mTitleHeight
        canvas.drawRect(left, top, right, bottom, mBgPaint)
        val baseLine = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaseLine
        canvas.drawText(mTitleMap[position]!!, builder.leftPadding, baseLine, mTextPaint)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val position = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) return
        val title = getTitle(position)
        if (TextUtils.isEmpty(title)) return

        val child = parent.findViewHolderForAdapterPosition(position)?.itemView
        var flag = false
        if (getTitle(position + 1) != null && !title.equals(getTitle(position + 1))) {
            if (child?.height!! + child.top < mTitleHeight) {
                c.save()
                flag = true
                c.translate(.0f, child.height + child.top - mTitleHeight)
            }
        }

        c.drawRect(parent.paddingLeft.toFloat(), parent.paddingTop.toFloat(),
                (parent.right - parent.paddingRight).toFloat(), parent.paddingTop + mTitleHeight, mBgPaint)

        c.drawRect(parent.paddingLeft.toFloat(), parent.paddingTop.toFloat() + mTitleHeight - DensityUtil.dip2px(3),
                (parent.right - parent.paddingRight).toFloat(), parent.paddingTop + mTitleHeight, mShadowPaint)

        val baseLine = parent.paddingTop + mTitleHeight - (mTitleHeight - mTextHeight) / 2 - mTextBaseLine
        c.drawText(title, builder.leftPadding, baseLine, mTextPaint)

        if (flag) c.restore()
    }

    private fun getTitle(position: Int): String? {
        var position = position
        while (position >= 0) {
            if (mTitleMap.containsKey(position)) {
                return mTitleMap[position]
            }
            position--
        }
        return null
    }


    fun dip2px(dpValue: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    class Builder(val context: Context?) {

        var textSize: Int? = null
        var textColor: Int? = null
        var backgroundColor: Int? = null
        var leftPadding: Float = .0f
        var topPadding: Float = .0f
        var rightPadding: Float = .0f
        var bottomPadding: Float = .0f
        var titleMap: Map<Int, String>? = null
        var recyclerView: RecyclerView? = null

        fun setBackgroundColor(color: Int): Builder {
            this.backgroundColor = color
            return this
        }

        fun setPadding(leftPadding: Float, topPadding: Float, rightPadding: Float, bottomPadding: Float): Builder {
            this.leftPadding = leftPadding
            this.topPadding = topPadding
            this.rightPadding = rightPadding
            this.bottomPadding = bottomPadding
            return this
        }

        fun setTextSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        fun setTextColor(color: Int): Builder {
            this.textColor = color
            return this
        }

        fun setTitleMap(titleMap: Map<Int, String>): Builder {
            this.titleMap = titleMap
            return this
        }

        fun setRecyclerView(recycler: RecyclerView?): Builder {
            this.recyclerView = recycler
            return this
        }

        fun build(): FloatingTitleItemDecoration {
            return FloatingTitleItemDecoration(this)
        }
    }

}