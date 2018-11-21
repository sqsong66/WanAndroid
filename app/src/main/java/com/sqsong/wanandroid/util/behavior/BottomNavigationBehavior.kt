package com.sqsong.wanandroid.util.behavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.sqsong.wanandroid.util.LogUtil

class BottomNavigationBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<BottomNavigationView>(context, attrs) {

    private var currentState = 2
    private var currentAnimator: ViewPropertyAnimator? = null

    override fun layoutDependsOn(parent: CoordinatorLayout, child: BottomNavigationView, dependency: View): Boolean {
        if (dependency is SnackbarLayout) {
            this.updateSnackBar(child, (dependency as SnackbarLayout?)!!)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }


    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView,
                                     directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView,
                                   target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = Math.max(0.0f, Math.min(child.height.toFloat(), child.translationY + dy))
        LogUtil.e("BottomNavigationBehavior", "dy -> $dy, child height -> ${child.height}, consumed y -> ${consumed[1]}")
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: BottomNavigationView, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        LogUtil.i("BottomNavigationBehavior", "onStopNestedScroll -> ${child.translationY}")
        val translationY = child.translationY
        if (translationY <= child.height / 2 && currentState != 2) {
            // showBottomNavigationView(child)
            slideUp(child)
        } else if (translationY > child.height / 2 && currentState != 1) {
            // hideBottomNavigationView(child)
            slideDown(child)
        }
    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(view.height.toFloat())
    }

    private fun showBottomNavigationView(view: BottomNavigationView) {
        view.animate().translationY(0f)
    }

    private fun slideUp(child: BottomNavigationView) {
        if (this.currentAnimator != null) {
            this.currentAnimator?.cancel()
            child.clearAnimation()
        }

        this.currentState = 2
        this.animateChildTo(child, 0, 225L, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
    }

    private fun slideDown(child: BottomNavigationView) {
        if (this.currentAnimator != null) {
            this.currentAnimator?.cancel()
            child.clearAnimation()
        }

        this.currentState = 1
        this.animateChildTo(child, child.height, 175L, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR)
    }

    private fun animateChildTo(child: BottomNavigationView, targetY: Int, duration: Long, interpolator: TimeInterpolator) {
        this.currentAnimator = child.animate().translationY(targetY.toFloat()).setInterpolator(interpolator).setDuration(duration).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                currentAnimator = null
            }
        })
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
}
