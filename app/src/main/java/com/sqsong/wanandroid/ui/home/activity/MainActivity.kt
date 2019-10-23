package com.sqsong.wanandroid.ui.home.activity

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.data.HotSearchData
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.home.mvp.MainContract
import com.sqsong.wanandroid.ui.home.mvp.MainPresenter
import com.sqsong.wanandroid.ui.search.SearchActivity
import com.sqsong.wanandroid.ui.settings.SettingActivity
import com.sqsong.wanandroid.ui.wechat.PublicAccountActivity
import com.sqsong.wanandroid.ui.welfare.WelfareActivity
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.SnackbarUtil
import com.sqsong.wanandroid.view.search.MaterialSearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_home.*

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View, NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, MaterialSearchView.OnSearchActionListener {

    private var lastClickTime: Long = 0

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initEvent() {
        configReveal()
        setupDrawerAndToolbar()
        searchView.setOnSearchActionListener(this)
        mPresenter.onAttach(this)
    }

    private fun configReveal() {
        if (intent.hasExtra("revealX") && intent.hasExtra("revealY")) {
            rootView.visibility = View.INVISIBLE

            val revealX = intent.getIntExtra("revealX", 0)
            val revealY = intent.getIntExtra("revealY", 0)
            val viewTreeObserver = rootView.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        revealActivity(revealX, revealY)
                        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }
    }

    private fun revealActivity(revealX: Int, revealY: Int) {
        val radius = rootView.width.coerceAtLeast(rootView.height)
        ViewAnimationUtils.createCircularReveal(rootView, revealX, revealY, 0f, radius.toFloat()).apply {
            duration = 500
            interpolator = AccelerateInterpolator()
            rootView.visibility = View.VISIBLE
            start()
        }
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
        navView.setCheckedItem(R.id.nav_home)
    }

    override fun showUserName(userName: String?) {
        val headerLayout = navView.getHeaderView(0)
        (headerLayout.findViewById<TextView>(R.id.nameTv)).text = if (!TextUtils.isEmpty(userName)) userName else getString(R.string.text_click_login)
        (headerLayout.findViewById<ImageView>(R.id.headIv)).setOnClickListener(if (TextUtils.isEmpty(userName)) this else null)
    }

    override fun getActivity(): AppCompatActivity = this

    override fun getFab(): FloatingActionButton = fab

    override fun getCurrentIndex(): Int = viewPager.currentItem

    override fun supportFragmentManager(): FragmentManager = supportFragmentManager

    override fun setPagerAdapter(adapter: FragmentStatePagerAdapter) {
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 4
    }

    override fun setupHotSearchKey(keyList: List<HotSearchData>?) {
        searchView.setHotSearchData(keyList)
    }

    override fun startNewActivity(intent: Intent) = startActivity(intent)

    override fun onSearch(key: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(Constants.SEARCH_KEY, key)
        startActivity(intent)
    }

    override fun onSearchViewVisible() {

    }

    override fun onSearchViewGone() {

    }

    override fun onTextChanged(text: String?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        val item = menu?.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.headIv) {
            mPresenter.loginOut()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_home, R.id.nav_home -> navigateToPage(0)
            R.id.action_knowledge -> navigateToPage(1)
            R.id.action_navigation -> navigateToPage(2)
            R.id.action_project -> navigateToPage(3)
            R.id.nav_public_account -> startActivity(Intent(this, PublicAccountActivity::class.java)) // 公众号
            R.id.nav_collection -> mPresenter.checkCollectionState()
            R.id.nav_welfare -> startActivity(Intent(this, WelfareActivity::class.java)) // 福利
            R.id.nav_scan -> searchView?.postDelayed({ mPresenter.checkCameraPermission() }, 200) // 扫码
            R.id.nav_setting -> startActivity(Intent(this, SettingActivity::class.java)) // 设置
            R.id.action_login_out -> mPresenter.checkLoginState()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToPage(index: Int) {
        val currentIndex = viewPager.currentItem
        if (currentIndex == index) return
        viewPager.setCurrentItem(index, false)
    }

    override fun switchBottomViewNavigation(@IdRes id: Int) {
        bottomNavigationView.selectedItemId = id
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            searchView.isSearchViewShow() -> searchView.closeSearchView()
            else -> quitApp()
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
        AlertDialog.Builder(this, R.style.DialogStyle)
                .setTitle(R.string.text_login_out_title)
                .setMessage(R.string.text_login_out_tips)
                .setCancelable(false)
                .setNegativeButton(R.string.text_cancel) { dialog, _ ->
                    run {
                        navView.setCheckedItem(R.id.nav_home)
                        dialog.dismiss()
                    }
                }
                .setPositiveButton(R.string.text_sure) { dialog, _ ->
                    run {
                        dialog.dismiss()
                        mPresenter.loginOut()
                    }
                }
                .create()
                .show()
    }

    override fun showCameraPermissionDialog() {
        AlertDialog.Builder(this, R.style.DialogStyle)
                .setTitle(getString(R.string.text_permission_tips))
                .setMessage(getString(R.string.text_camera_permission_message))
                .setCancelable(false)
                .setNegativeButton(R.string.text_cancel) { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }
                .setPositiveButton(getString(R.string.text_open)) { dialog, _ ->
                    run {
                        dialog.dismiss()
                        startPermissionActivity()
                    }
                }
                .create()
                .show()
    }

    private fun startPermissionActivity() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts(getString(R.string.text_package_scheme), packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

}
