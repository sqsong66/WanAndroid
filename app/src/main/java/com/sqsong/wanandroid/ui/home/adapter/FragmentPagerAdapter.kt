package com.sqsong.wanandroid.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentPagerAdapter(fm: FragmentManager,
                           fragmentList: MutableList<Fragment>) : FragmentStatePagerAdapter(fm) {

    private val mFragmentList = fragmentList

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
}