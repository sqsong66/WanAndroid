/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqsong.wanandroid.util.ext


import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sqsong.wanandroid.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun AppCompatActivity.setupToolbar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun AppCompatActivity.setupUi(rootView: View) {
    if (rootView !is EditText) {
        rootView.setOnTouchListener { view, _ ->
            var inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            false
        }
    }

    if (rootView is ViewGroup) {
        for (i in 0..(rootView.childCount - 1)) {
            setupUi(rootView.getChildAt(i))
        }
    }
}

fun AppCompatActivity.registerClickEvent(view: View, action: (view: View) -> Unit): Disposable {
    return RxView.clicks(view)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action(view) }
}

fun AppCompatActivity.registerTextChangeEvent(textView: TextView, inputLayout: TextInputLayout): Disposable {
    return RxTextView.textChanges(textView)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.toString().isNotEmpty() && inputLayout.isErrorEnabled) {
                    inputLayout.isErrorEnabled = false
                }
            }
}

@SuppressLint("ResourceType")
fun AppCompatActivity.setupSwipeLayoutColor(swipeRefreshLayout: SwipeRefreshLayout) {
    val ta = obtainStyledAttributes(TypedValue().data,
            intArrayOf(R.attr.colorPrimaryLight, R.attr.colorPrimary, R.attr.colorPrimaryDark))
    val lightColor = ta?.getColor(0, ContextCompat.getColor(this, R.color.colorPrimaryLight))
    val primaryColor = ta?.getColor(1, ContextCompat.getColor(this, R.color.colorPrimary))
    val primaryDarkColor = ta?.getColor(2, ContextCompat.getColor(this, R.color.colorPrimaryDark))
    swipeRefreshLayout.setColorSchemeColors(lightColor!!, primaryColor!!, primaryDarkColor!!)
    ta.recycle()
}

fun AppCompatActivity.clickObservable(@IdRes id: Int): Observable<Any> {
    return RxView.clicks(findViewById(id))
}