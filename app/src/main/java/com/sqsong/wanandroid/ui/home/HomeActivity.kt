package com.sqsong.wanandroid.ui.home

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.ui.home.adapter.FragmentPagerAdapter
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.fragment.KnowledgeFragment
import com.sqsong.wanandroid.ui.home.fragment.NavigationFragment
import com.sqsong.wanandroid.ui.home.fragment.ProjectFragment
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.util.SnackbarUtil
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_new.*
import javax.inject.Inject

@ChangeThemeAnnotation
class HomeActivity : DaggerAppCompatActivity(), IAppCompatActivity, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var mThemeDialog: ThemeSwitcherDialog

    @Inject
    lateinit var mHomeFragment: HomeFragment

    @Inject
    lateinit var mKnowledgeFragment: KnowledgeFragment

    @Inject
    lateinit var mNavigationFragment: NavigationFragment

    @Inject
    lateinit var mProjectFragment: ProjectFragment

    private val mFragmentList = mutableListOf<Fragment>()

    override fun getLayoutResId(): Int {
        return R.layout.activity_home_new
    }

    override fun initEvent() {
        setupDrawer()
        setupFragments()
    }

    private fun setupDrawer() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        fab.setOnClickListener {
            SnackbarUtil.showSnackText(fab, "Fab clicked")
            // SnackbarUtil.showToastText(it.context, "Fab clicked")
        }
    }

    private fun setupFragments() {
        mFragmentList.add(mHomeFragment)
        mFragmentList.add(mKnowledgeFragment)
        mFragmentList.add(mNavigationFragment)
        mFragmentList.add(mProjectFragment)
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

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
