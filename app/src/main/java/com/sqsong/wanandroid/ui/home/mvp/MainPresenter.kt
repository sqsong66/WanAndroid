package com.sqsong.wanandroid.ui.home.mvp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.sqsong.wanandroid.BaseApplication
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.FragmentPagerAdapter
import com.sqsong.wanandroid.common.event.FabClickEvent
import com.sqsong.wanandroid.common.event.SwitchIndexEvent
import com.sqsong.wanandroid.data.HotSearchBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.mvp.IModel
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.CookieManager
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.collection.CollectionActivity
import com.sqsong.wanandroid.ui.home.fragment.HomeFragment
import com.sqsong.wanandroid.ui.home.fragment.KnowledgeFragment
import com.sqsong.wanandroid.ui.home.fragment.NavigationFragment
import com.sqsong.wanandroid.ui.home.fragment.ProjectFragment
import com.sqsong.wanandroid.ui.login.LoginActivity
import com.sqsong.wanandroid.ui.scan.ScanningActivity
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.RxJavaHelper
import com.sqsong.wanandroid.util.permission.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class MainPresenter @Inject constructor(private val mainView: MainContract.View,
                                        private val mainModel: MainModel,
                                        private val disposable: CompositeDisposable) :
        BasePresenter<MainContract.View, IModel>(mainModel, disposable) {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mPreferences: SharedPreferences

    @Inject
    lateinit var mHomeFragment: HomeFragment

    @Inject
    lateinit var mKnowledgeFragment: KnowledgeFragment

    @Inject
    lateinit var mNavigationFragment: NavigationFragment

    @Inject
    lateinit var mProjectFragment: ProjectFragment

    private var mFragmentList = mutableListOf<Fragment>()

    private var mPermissions: RxPermissions? = null

    override fun onAttach(view: MainContract.View) {
        mView = mainView
        registerEvent()
    }

    private fun registerEvent() {
        EventBus.getDefault().register(this)
        mView?.showUserName(mPreferences[Constants.LOGIN_USER_NAME])
        addDisposable(fabDisposable())
        setupFragments()
        requestHotKey()
    }

    private fun addDisposable(dispos: Disposable?) {
        if (dispos != null) {
            disposable.add(dispos)
        }
    }

    private fun fabDisposable(): Disposable? {
        if (mView == null) return null
        return RxView.clicks(mView!!.getFab())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    EventBus.getDefault().post(FabClickEvent(mView?.getCurrentIndex() ?: 0))
                }
    }

    private fun setupFragments() {
        mFragmentList.clear()
        mFragmentList.add(mHomeFragment)
        mFragmentList.add(mKnowledgeFragment)
        mFragmentList.add(mNavigationFragment)
        mFragmentList.add(mProjectFragment)
        val pagerAdapter = FragmentPagerAdapter(mView?.supportFragmentManager(), mFragmentList)
        mView?.setPagerAdapter(pagerAdapter)
    }

    fun checkLoginState() {
        val userName: String = mPreferences[Constants.LOGIN_USER_NAME] ?: ""
        if (TextUtils.isEmpty(userName)) {
            loginOut()
        } else {
            mView?.showLoginOutTipDialog()
        }
    }

    fun loginOut() {
        BaseApplication.INSTANCE.quitApp()
        CookieManager.getInstance(mContext).clearCookieInfo()
        mView?.startNewActivity(Intent(mView?.getActivity(), LoginActivity::class.java))
    }

    fun checkCollectionState() {
        val userName: String = mPreferences[Constants.LOGIN_USER_NAME] ?: ""
        if (TextUtils.isEmpty(userName)) {
            mView?.showLoginOutTipDialog()
        } else {
            mView?.startNewActivity(Intent(mView?.getActivity(), CollectionActivity::class.java))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSwitchIndex(event: SwitchIndexEvent) {
        mView?.switchBottomViewNavigation(analysisSelectedItemId(event.index))
    }

    private fun analysisSelectedItemId(index: Int): Int {
        return when (index) {
            0 -> R.id.action_home
            1 -> R.id.action_knowledge
            2 -> R.id.action_navigation
            else -> R.id.action_project
        }
    }

    private fun requestHotKey() {
        mainModel.getHotKey()
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<HotSearchBean>(disposable) {
                    override fun onSuccess(bean: HotSearchBean) {
                        if (bean.errorCode == 0) {
                            mView?.setupHotSearchKey(bean.data)
                        } else {
                            mView?.showMessage(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        mView?.showMessage(error.showMessage)
                    }
                })
    }

    fun checkCameraPermission() {
        if (mPermissions == null) {
            mPermissions = RxPermissions(mView?.getActivity()!!)
        }
        disposable.add(mPermissions!!.requestEach(Manifest.permission.CAMERA)
                .subscribe {
                    when {
                        it.granted -> {
                            mView?.startNewActivity(Intent(mView?.getActivity(), ScanningActivity::class.java))
                        }
                        it.shouldShowRequestPermissionRationale -> {
                            mView?.showMessage(mContext.getString(R.string.text_camera_permission))
                        }
                        else -> {
                            mView?.showCameraPermissionDialog()
                        }
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}