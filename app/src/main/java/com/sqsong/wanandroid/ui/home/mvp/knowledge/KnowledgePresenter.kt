package com.sqsong.wanandroid.ui.home.mvp.knowledge

import com.sqsong.wanandroid.common.event.FabClickEvent
import com.sqsong.wanandroid.common.inter.OnItemClickListener
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.BasePresenter
import com.sqsong.wanandroid.network.ApiException
import com.sqsong.wanandroid.network.ObserverImpl
import com.sqsong.wanandroid.ui.home.adapter.KnowledgeAdapter
import com.sqsong.wanandroid.util.RxJavaHelper
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class KnowledgePresenter @Inject constructor(private val knowledgeModel: KnowledgeModel, private val disposable: CompositeDisposable) :
        BasePresenter<KnowledgeContract.View, KnowledgeContract.Model>(knowledgeModel, disposable), OnItemClickListener<KnowledgeData> {

    private val mDataList = mutableListOf<KnowledgeData>()
    private val mAdapter by lazy {
        KnowledgeAdapter(mView?.getFragmentContext(), mDataList)
    }

    override fun onAttach(view: KnowledgeContract.View) {
        super.onAttach(view)
        mView?.showLoadingPage()
        initAdapter()
    }

    private fun initAdapter() {
        EventBus.getDefault().register(this)
        mView?.setRecyclerAdapter(mAdapter)
        mAdapter.setOnItemClickListener(this)
    }

    fun requestKnowledgeData() {
        knowledgeModel.getKnowledgeList()
                .compose(RxJavaHelper.compose())
                .doOnEach { mView?.showContentPage() }
                .subscribe(object : ObserverImpl<KnowledgeBean>(disposable) {
                    override fun onSuccess(bean: KnowledgeBean) {
                        if (bean.errorCode == 0) {
                            processKnowledgeData(bean.data)
                        } else {
                            mView?.showMessage(bean.errorMsg)
                            mView?.showErrorPage()
                        }
                    }

                    override fun onFail(error: ApiException) {
                        mView?.showMessage(error.showMessage)
                        mView?.showErrorPage()
                    }

                })
    }

    private fun processKnowledgeData(dataList: List<KnowledgeData>?) {
        mView?.showContentPage()
        if (dataList == null || dataList.isEmpty()) {
            mView?.showEmptyPage()
            return
        }
        mDataList.clear()
        mDataList.addAll(dataList)
        mAdapter.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFabClick(event: FabClickEvent) {
        if (event.index == 1) {
            mView?.scrollRecycler(0)
        }
    }

    override fun onItemClick(data: KnowledgeData?, position: Int) {
        mView?.startKnowledgeActivity(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}