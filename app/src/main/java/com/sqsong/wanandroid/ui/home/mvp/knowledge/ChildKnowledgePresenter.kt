package com.sqsong.wanandroid.ui.home.mvp.knowledge

import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.mvp.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChildKnowledgePresenter @Inject constructor(private val knowledgeModel: ChildKnowledgeModel,
                                                  private val disposable: CompositeDisposable) :
        BasePresenter<ChiledKnowledgeContract.View, ChildKnowledgeModel>(knowledgeModel, disposable) {

    private lateinit var mKnowledgeData: KnowledgeData

    override fun onAttach(view: ChiledKnowledgeContract.View) {
        super.onAttach(view)

    }

}