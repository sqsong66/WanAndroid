package com.sqsong.wanandroid.home

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.home.adapter.FragmentPagerAdapter
import com.sqsong.wanandroid.home.fragment.HomeFragment
import com.sqsong.wanandroid.home.fragment.KnowledgeFragment
import com.sqsong.wanandroid.home.fragment.NavigationFragment
import com.sqsong.wanandroid.home.fragment.ProjectFragment
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import javax.inject.Inject

@ChangeThemeAnnotation
class HomeActivity : DaggerAppCompatActivity(), IAppCompatActivity {

    @Inject
    lateinit var mThemeDialog: ThemeSwitcherDialog

    private val mFragmentList = mutableListOf<Fragment>()

    override fun getLayoutResId(): Int {
        return R.layout.activity_home
    }

    override fun initEvent() {
        setSupportActionBar(toolbar)
        setupFragments()
    }

    private fun setupFragments() {
        mFragmentList.add(HomeFragment())
        mFragmentList.add(KnowledgeFragment())
        mFragmentList.add(NavigationFragment())
        mFragmentList.add(ProjectFragment())
        val fragmentPagerAdapter = FragmentPagerAdapter(supportFragmentManager, mFragmentList)
        viewPager.adapter = fragmentPagerAdapter
        viewPager.offscreenPageLimit = 4

        toolbar.post { toolbar.title = getString(R.string.text_home) }
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> navigateToPage(0, it.title)
                R.id.action_knowledge -> navigateToPage(1, it.title)
                R.id.action_navigation -> navigateToPage(2, it.title)
                R.id.action_project -> navigateToPage(3, it.title)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun navigateToPage(index: Int, title: CharSequence) {
        val currentIndex = viewPager.currentItem
        if (currentIndex == index) return
        viewPager.setCurrentItem(index, false)
        toolbar.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_theme) {
            mThemeDialog.show(supportFragmentManager, "")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
