package com.sqsong.wanandroid.theme

import android.app.Activity
import androidx.annotation.StyleRes
import com.sqsong.wanandroid.base.BaseApplication

class ThemeOverlayUtil {

    companion object {

        @StyleRes
        var mThemeOverlays: Int? = 0

        fun setThemeOverlay(activity: Activity?, themeOverlays: Int?) {
            if (ThemeOverlayUtil.mThemeOverlays != themeOverlays) {
                ThemeOverlayUtil.mThemeOverlays = themeOverlays
                // activity?.recreate()
                val activityList = BaseApplication.INSTANCE.getActivityList()
                if (activityList != null && !activityList.isEmpty()) {
                    for (activity in activityList) {
                        activity.recreate()
                    }
                }
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