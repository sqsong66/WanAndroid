package com.sqsong.wanandroid.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentPagerAdapter(fm: FragmentManager?,
                           fragmentList: MutableList<Fragment>, private val titleList: List<String>? = null) :
        FragmentStatePagerAdapter(fm) {

    private val mFragmentList = fragmentList

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList?.get(position)
    }
}