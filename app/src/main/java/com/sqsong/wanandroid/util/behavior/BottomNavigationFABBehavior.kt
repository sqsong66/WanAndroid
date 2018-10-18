package com.sqsong.wanandroid.util.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import me.jessyan.autosize.utils.LogUtils

class BottomNavigationFABBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View) {
        child.translationY = 0.0f
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL && super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            child.hide()
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            child.show()
        }
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        return this.updateButton(parent, child, dependency)
    }

    private fun updateButton(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        return if (dependency is SnackbarLayout) {
            /* // this is the translation action.
            val oldTranslation = child.translationY
            val height = dependency.getHeight().toFloat()
            val newTranslation = dependency.getTranslationY() - height
            child.translationY = newTranslation
            oldTranslation != newTranslation*/
            val translationY = getFabTranslationYForSnackbar(parent, child)
            val percentComplete = -translationY / dependency.getHeight()
            val scaleFactor = 1 - percentComplete

            child.scaleX = scaleFactor
            child.scaleY = scaleFactor
            return false
        } else {
            var factor = 1 - dependency.translationY / dependency.height
            child.scaleX = factor
            child.scaleY = factor
            if (factor == 0f && child.visibility == View.VISIBLE) {
                child.visibility = View.GONE
            } else if (factor > 0 && child.visibility == View.GONE) {
                child.visibility = View.VISIBLE
            }
            LogUtils.e("Dependency view translationY: ${dependency.translationY}, Child factor: $factor")
            false
        }
    }

    private fun getFabTranslationYForSnackbar(parent: CoordinatorLayout, fab: FloatingActionButton): Float {
        var minOffset = 0f
        val dependencies = parent.getDependencies(fab)
        var i = 0
        val z = dependencies.size
        while (i < z) {
            val view = dependencies[i]
            if (view is Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - view.getHeight())
            }
            i++
        }
        return minOffset
    }
}
