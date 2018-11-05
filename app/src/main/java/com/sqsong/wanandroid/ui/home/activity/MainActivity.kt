package com.sqsong.wanandroid.ui.home.activity

import android.content.Intent
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.common.inter.IAppCompatActivity
import com.sqsong.wanandroid.theme.ThemeSwitcherDialog
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.home.mvp.MainContract
import com.sqsong.wanandroid.ui.home.mvp.MainPresenter
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.ui.wechat.PublicAccountActivity
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.SnackbarUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_home.*
import javax.inject.Inject

@ChangeThemeAnnotation
class MainActivity : BaseActivity<MainPresenter>(), MainContract.View,
        IAppCompatActivity, NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @Inject
    lateinit var mThemeDialog: ThemeSwitcherDialog

    private var lastClickTime: Long = 0

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initEvent() {
        setupDrawerAndToolbar()
        mPresenter.onAttach(this)
    }

    private fun setupDrawerAndToolbar() {
        setSupportActionBar(toolbar)
        toolbar.post { toolbar.title = getString(R.string.text_home) }
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun showUserName(userName: String?) {
        val headerLayout = navView.getHeaderView(0)
        (headerLayout.findViewById<TextView>(R.id.nameTv)).text = if (!TextUtils.isEmpty(userName)) userName else getString(R.string.text_click_login)
        (headerLayout.findViewById<ImageView>(R.id.headIv)).setOnClickListener(if (TextUtils.isEmpty(userName)) this else null)
    }

    override fun getFab(): FloatingActionButton {
        return fab
    }

    override fun getCurrentIndex(): Int {
        return viewPager.currentItem
    }

    override fun supportFragmentManager(): FragmentManager {
        return supportFragmentManager
    }

    override fun setPagerAdapter(adapter: FragmentStatePagerAdapter) {
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 4
    }

    override fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.headIv) {
            mPresenter.loginOut()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_theme -> {
                mThemeDialog.show(supportFragmentManager(), "")
                return true
            }
            R.id.action_login_out -> {
                mPresenter.checkLoginState()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_home -> navigateToPage(0, menuItem.title)
            R.id.action_knowledge -> navigateToPage(1, menuItem.title)
            R.id.action_navigation -> navigateToPage(2, menuItem.title)
            R.id.action_project -> navigateToPage(3, menuItem.title)
            R.id.nav_public_account -> startActivity(Intent(this, PublicAccountActivity::class.java)) // 公众号
//            R.id.nav_slideshow -> {
//            }
//            R.id.nav_manage -> {
//            }
            R.id.nav_share -> {
            }
            R.id.nav_send -> {
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToPage(index: Int, title: CharSequence) {
        val currentIndex = viewPager.currentItem
        if (currentIndex == index) return
        viewPager.setCurrentItem(index, false)
    }

    override fun switchBottomViewNavigation(@IdRes id: Int) {
        bottomNavigationView.selectedItemId = id
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            quitApp()
        }
    }

    private fun quitApp() {
        val curMillis = System.currentTimeMillis()
        if (curMillis - lastClickTime > Constants.APP_QUIT_TIME) {
            SnackbarUtil.showNormalToast(this, getString(R.string.text_double_click_quit))
            lastClickTime = curMillis
        } else {
            BaseApplication.INSTANCE.quitApp()
        }
    }

    override fun showLoginOutTipDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.text_login_out_title)
                .setMessage(R.string.text_login_out_tips)
                .setCancelable(false)
                .setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.text_sure) { dialog, _ ->
                    run {
                        dialog.dismiss()
                        mPresenter.loginOut()
                    }
                }
                .create()
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
