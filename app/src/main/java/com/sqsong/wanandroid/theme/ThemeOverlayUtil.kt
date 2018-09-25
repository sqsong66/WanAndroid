package com.sqsong.wanandroid.theme

import android.app.Activity
import androidx.annotation.StyleRes
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation

class ThemeOverlayUtil {

    companion object {

        @StyleRes
        var mThemeOverlays: Int? = 0

        fun setThemeOverlay(themeOverlays: Int?, activityList: List<Activity?>) {
            if (ThemeOverlayUtil.mThemeOverlays != themeOverlays) {
                for (activity in activityList) {
                    if ((activity?.javaClass?.isAnnotationPresent(ChangeThemeAnnotation::class.java))!!)
                        activity?.recreate()
                }
                ThemeOverlayUtil.mThemeOverlays = themeOverlays
            }
        }

        fun getThemeOverlay(): Int? {
            return mThemeOverlays
        }

        fun applyThemeOverlay(activity: Activity) {
            if (mThemeOverlays != 0) {
                activity.setTheme(mThemeOverlays!!)
            }
        }
    }
}