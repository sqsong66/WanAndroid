package com.sqsong.wanandroid.ui.home.mvp.knowledge

import com.sqsong.wanandroid.mvp.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChiledKnowledgePresenter @Inject constructor(private val knowledgeModel: ChildKnowlegeModel,
                                                   private val disposable: CompositeDisposable) :
        BasePresenter<ChiledKnowledgeContract.View, ChildKnowlegeModel>(knowledgeModel, disposable) {

    override fun onAttach(view: ChiledKnowledgeContract.View) {
        super.onAttach(view)

    }

}