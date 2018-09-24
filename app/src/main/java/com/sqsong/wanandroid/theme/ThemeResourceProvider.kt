package com.sqsong.wanandroid.theme

import androidx.annotation.ArrayRes
import com.sqsong.wanandroid.R

class ThemeResourceProvider {

    @ArrayRes
    fun getThemeColors(): Int {
        return R.array.theme_palette_array
    }

    @ArrayRes
    fun getThemeColorDesc(): Int {
        return R.array.theme_palette_desc_array
    }

}