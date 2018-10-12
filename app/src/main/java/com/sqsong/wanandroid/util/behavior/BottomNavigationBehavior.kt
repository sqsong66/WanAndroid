package com.sqsong.wanandroid.util.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.sqsong.wanandroid.util.LogUtil

class BottomNavigationBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: BottomNavigationView, dependency: View): Boolean {
        /*if (dependency is ViewPager) {
            this.updateViewPager(parent, child, dependency)
        }*/

        if (dependency is SnackbarLayout) {
            this.updateSnackBar(child, (dependency as SnackbarLayout?)!!)
        }
        return super.layoutDependsOn(parent!!, child, dependency!!)
    }


    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView,
                                     directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView,
                                   target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        // LogUtil.e("translationY: ${child.translationY}, dy: $dy, target: $target")
         child.translationY = Math.max(0.0f, Math.min(child.height.toFloat(), child.translationY + dy))
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: BottomNavigationView, dependency: View): Boolean {
        LogUtil.e("dependency: $dependency")
        return super.onDependentViewChanged(parent, child, dependency)
    }

    private fun updateSnackBar(child: BottomNavigationView, snackbarLayout: SnackbarLayout) {
        if (snackbarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
            val layoutParams = snackbarLayout.layoutParams
                    ?: throw RuntimeException("null cannot be cast to non-null type android.support.design.widget.CoordinatorLayout.LayoutParams")

            val params = layoutParams as CoordinatorLayout.LayoutParams
            params.anchorId = child.id
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            params.bottomMargin = (child.height - child.translationY + params.leftMargin).toInt()
            snackbarLayout.layoutParams = params
        }

    }

    /*private fun updateViewPager(parent: CoordinatorLayout, child: BottomNavigationView, dependency: ViewPager) {
        if (ViewCompat.isLaidOut(parent)) {
            //attach our bottom view to the bottom of CoordinatorLayout
            child.y = (parent.bottom - child.height).toFloat()

            //set bottom padding to the dependency view to prevent bottom view from covering it
            dependency.setPadding(dependency.paddingLeft, dependency.paddingTop,
                    dependency.paddingRight, child.height + DensityUtil.getToolbarHeight(dependency.context))
        }
    }*/
}
