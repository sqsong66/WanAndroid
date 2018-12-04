package com.sqsong.wanandroid.ui.search.mvp

import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.holder.LoadingFooterViewHolder
import com.sqsong.wanandroid.data.BaseData
import com.sqsong.wanandroid.data.HomeItem
import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.adapter.HomeItemAdapter
import com.sqsong.wanandroid.ui.search.adapter.SearchAdapter
import com.sqsong.wanandroid.ui.web.WebViewActivity
import com.sqsong.wanandroid.util.CommonUtil
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.LogUtil
import com.sqsong.wanandroid.util.PreferenceHelper.get
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchPresenter @Inject constructor(private val searchModel: SearchModel,
                                          private val disposable: CompositeDisposable) :
        BasePresenter<SearchContract.View, SearchContract.Model>(searchModel, disposable), HomeItemAdapter.HomeItemActionListener {

    @Inject
    lateinit var mPreferences: SharedPreferences

    private var mPage: Int = 0
    private lateinit var mCurrentKey: String
    private val mDataList = mutableListOf<HomeItem>()
    private val mAdapter: SearchAdapter by lazy {
        SearchAdapter(mView?.getAppContext(), mDataList)
    }

    override fun onAttach(view: SearchContract.View) {
        super.onAttach(view)
        init()
    }

    private fun init() {
        mView?.showLoadingPage()
        mView?.setRecyclerAdapter(mAdapter)
        mAdapter.setHomeItemActionListener(this)
        addDisposable(searchDisposable())
        startQuery(mView?.getInitKey())
    }

    private fun addDisposable(dispos: Disposable?) {
        if (dispos != null) {
            disposable.add(dispos)
        }
    }

    private fun searchDisposable(): Disposable? {
        if (mView == null) return null
        return mView!!.searchObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    LogUtil.e("SearchPresenter", "Start query: $it")
                    mPage = 0
                    mView?.showClear(!it.isEmpty())
                    startQuery(it.toString())
                }
    }

    fun refreshData() {
        mPage = 0
        startQuery(mCurrentKey)
    }

    fun loadMore() {
        mPage++
        startQuery(mCurrentKey)
    }

    private fun startQuery(key: String?) {
        if (TextUtils.isEmpty(key)) {
            mDataList.clear()
            mAdapter.notifyDataSetChanged()
            mView?.showEmptyPage()
            return
        }
        mCurrentKey = key!!
        mAdapter.setSearchKey(mCurrentKey)
        searchModel.query(mPage, key!!)
                .compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<HomeItemBean>(disposable) {
                    override fun onSuccess(bean: HomeItemBean) {
                        if (bean.errorCode == 0) {
                            setupDataList(bean.data?.datas)
                            if (mPage == 0) {
                                mAdapter.setSearchResult(bean.data?.total ?: 0)
                            }
                        } else {
                            showErrors(bean.errorMsg!!)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        showErrors(error.showMessage)
                    }
                })
    }

    private fun showErrors(message: String) {
        mView?.showMessage(message)
        if (mPage == 0) {
            mView?.showErrorPage()
        } else {
            mPage--
            mView?.loadFinish()
        }
    }

    private fun setupDataList(dataList: List<HomeItem>?) {
        mView?.showContentPage()
        if (dataList == null || dataList.isEmpty()) {
            if (mPage == 0) {
                mView?.showEmptyPage()
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            }
            return
        }
        if (mPage == 0) {
            mDataList.clear()
        }
        mDataList.addAll(dataList)
        mAdapter.notifyDataSetChanged()

        mView?.getHandler()?.post {
            if (mPage == 0 && mView?.findRecyclerLastVisibleItemPosition() == mDataList.size + 1) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_HIDDEN)
            } else if (dataList.size < Constants.PAGE_SIZE) {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_NO_CONTENT)
            } else {
                mAdapter.updateLoadingState(LoadingFooterViewHolder.STATE_LOADING)
                mView?.loadFinish()
            }
        }
    }

    override fun onStarClick(homeItem: HomeItem, position: Int) {
        val userName: String = mPreferences[Constants.LOGIN_USER_NAME] ?: ""
        if (TextUtils.isEmpty(userName)) {
            mView?.showLoginDialog()
            return
        }

        val collectState = homeItem.collect
        val requestObservable = if (collectState) searchModel.unCollectArticle(homeItem.id) else searchModel.collectArticle(homeItem.id)
        requestObservable.compose(RxJavaHelper.compose())
                .subscribe(object : ObserverImpl<BaseData>(disposable) {
                    override fun onSuccess(bean: BaseData) {
                        if (bean.errorCode == 0) {
                            if (collectState) {
                                homeItem.collect = false
                                mView?.showMessage(mView?.getStringFromResource(R.string.text_cancel_collect_success))
                            } else {
                                homeItem.collect = true
                                mView?.showMessage(mView?.getStringFromResource(R.string.text_collect_success))
                            }
                            mAdapter.notifyItemChanged(position)
                        } else {
                            mView?.showMessage(bean.errorMsg)
                        }
                    }

                    override fun onFail(error: ApiException) {
                        mView?.showMessage(error.showMessage)
                    }
                })
    }

    override fun onListItemClick(homeItem: HomeItem, position: Int) {
        val intent = Intent(mView?.getAppContext(), WebViewActivity::class.java)
        intent.putExtra(Constants.KEY_WEB_URL, homeItem.link)
        intent.putExtra(Constants.KEY_WEB_TITLE, homeItem.title)
        mView?.startNewActivity(intent)
    }

    override fun onShareClick(homeItem: HomeItem, position: Int) {
        val sharingIntent = CommonUtil.buildShareIntent(homeItem.title, homeItem.link)
        mView?.startNewActivity(Intent.createChooser(sharingIntent, mView?.getStringFromResource(R.string.text_share_link)))
    }

}