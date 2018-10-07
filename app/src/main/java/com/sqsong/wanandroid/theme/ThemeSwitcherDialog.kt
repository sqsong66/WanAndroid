package com.sqsong.wanandroid.theme

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.base.BaseApplication
import com.sqsong.wanandroid.common.GridSpaceItemDecoration
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.DensityUtil
import com.sqsong.wanandroid.util.PreferenceHelper
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.PreferenceHelper.set
import com.sqsong.wanandroid.view.CircleView

class ThemeSwitcherDialog : DialogFragment(), OnItemClickListener<ColorPalette> {

    private var mCheckedPos = 0
    private var mColorPalette: ColorPalette? = null
    private var mAdapter: ThemeColorAdapter? = null
    private lateinit var mPreferences: SharedPreferences
    private var mThemeOverlayList = mutableListOf<ColorPalette>()
    private lateinit var mThemeResourceProvider: ThemeResourceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreferences = PreferenceHelper.defaultPrefs(context!!)
        mThemeResourceProvider = ThemeResourceProvider()
        initializeColors()
    }

    @SuppressLint("ResourceType")
    private fun initializeColors() {
        val colorTypedArray = resources.obtainTypedArray(mThemeResourceProvider.getThemeColors())
        val descTypedArray = resources.obtainTypedArray(mThemeResourceProvider.getThemeColorDesc())
        if (colorTypedArray.length() != descTypedArray.length()) {
            throw IllegalArgumentException("Color array length not match description array length.")
        }
        mThemeOverlayList.clear()
        for (i in 0 until colorTypedArray.length()) {
            @StyleRes val paletteOverlay = colorTypedArray.getResourceId(i, 0)
            val typedArray = context?.obtainStyledAttributes(paletteOverlay, THEME_OVERLAY_ATTRS)
            val primaryColor = typedArray?.getColor(0, Color.TRANSPARENT)
            val primaryDarkColor = typedArray?.getColor(1, Color.TRANSPARENT)
            val secondaryColor = typedArray?.getColor(2, Color.TRANSPARENT)

            mCheckedPos = mPreferences[Constants.THEMEOVERLAY_INDEX] ?: 0
            val colorPalette = ColorPalette(paletteOverlay, primaryColor,
                    primaryDarkColor, secondaryColor, descTypedArray.getString(i), i == mCheckedPos)
            mThemeOverlayList.add(colorPalette)
            typedArray?.recycle()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let { AlertDialog.Builder(it) }?.run {
            setTitle(R.string.text_theme_switcher)
            setView(createDialogView(activity?.layoutInflater))
            setNegativeButton(R.string.text_cancel, null)
            setPositiveButton(R.string.text_save) { _, _ ->
                mPreferences[Constants.THEMEOVERLAY_INDEX] = mCheckedPos
                ThemeOverlayUtil.setThemeOverlay(mColorPalette?.themeOverlay, BaseApplication.INSTANCE.getActivityList())
                dismiss()
            }
            create()
        }

        return dialog ?: super.onCreateDialog(savedInstanceState)
    }

    private fun createDialogView(inflater: LayoutInflater?): View {
        val recycler: RecyclerView = inflater?.inflate(R.layout.dialog_theme_switcher, null) as RecyclerView
        val decoration = GridSpaceItemDecoration(4, DensityUtil.dip2px(16), true)
        recycler.layoutManager = GridLayoutManager(context, 4)
        recycler.addItemDecoration(decoration)
        mAdapter = ThemeColorAdapter(context, mThemeOverlayList)
        mAdapter?.setOnItemClickListener(this)
        recycler.adapter = mAdapter
        return recycler
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun onItemClick(data: ColorPalette?, position: Int) {
        if (position < 0 || position >= mThemeOverlayList.size) return
        if (mCheckedPos == position) return
        mThemeOverlayList[mCheckedPos].isChecked = false
        mThemeOverlayList[position].isChecked = true
        mAdapter?.notifyItemChanged(mCheckedPos)
        mAdapter?.notifyItemChanged(position)
        mCheckedPos = position

        mColorPalette = data
    }

    companion object {
        val THEME_OVERLAY_ATTRS = intArrayOf(R.attr.colorPrimary, R.attr.colorPrimaryDark, R.attr.colorSecondary)
    }
}

data class ColorPalette(
        @StyleRes val themeOverlay: Int,
        @ColorInt val primaryColor: Int?,
        @ColorInt val primaryDarkColor: Int?,
        @ColorInt val secondaryColor: Int?,
        val colorDesc: String,
        var isChecked: Boolean)

class ThemeColorAdapter(context: Context?, themeOverlayList: MutableList<ColorPalette>) : RecyclerView.Adapter<ThemeColorAdapter.ThemeColorViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mThemeOverlay: MutableList<ColorPalette> = themeOverlayList
    private var mListener: OnItemClickListener<ColorPalette>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeColorViewHolder {
        return ThemeColorViewHolder(mInflater.inflate(R.layout.item_theme_color, parent, false))
    }

    override fun getItemCount(): Int {
        return mThemeOverlay.size
    }

    override fun onBindViewHolder(holder: ThemeColorViewHolder, position: Int) {
        val colorPalette = mThemeOverlay[position]
        holder.primaryCircle.setBgColor(colorPalette.primaryColor)
        holder.primaryDarkCircle.setBgColor(colorPalette.primaryDarkColor)
        if (colorPalette.isChecked) {
            holder.doneImage.visibility = View.VISIBLE
        } else {
            holder.doneImage.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            mListener?.onItemClick(colorPalette, position)
        }
    }

    open fun setOnItemClickListener(listener: OnItemClickListener<ColorPalette>) {
        this.mListener = listener
    }

    class ThemeColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val primaryCircle: CircleView = itemView.findViewById(R.id.primary_circle)
        val primaryDarkCircle: CircleView = itemView.findViewById(R.id.primary_dar_circle)
        val doneImage: ImageView = itemView.findViewById(R.id.done_iv)
    }

}