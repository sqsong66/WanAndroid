package com.sqsong.wanandroid.ui.home.activity

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.home.mvp.knowledge.ChiledKnowledgeContract
import com.sqsong.wanandroid.ui.home.mvp.knowledge.ChiledKnowledgePresenter

@ChangeThemeAnnotation
class KnowledgeActivity : ChiledKnowledgeContract.View, BaseActivity<ChiledKnowledgePresenter>() {

    override fun beforeInflateView() {

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_knowledge
    }

    override fun initEvent() {
        mPresenter.onAttach(this)
    }


}
