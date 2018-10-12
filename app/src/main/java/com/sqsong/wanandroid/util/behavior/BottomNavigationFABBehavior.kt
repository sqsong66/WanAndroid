package com.sqsong.wanandroid.util.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class BottomNavigationFABBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View) {
        child.translationY = 0.0f
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
