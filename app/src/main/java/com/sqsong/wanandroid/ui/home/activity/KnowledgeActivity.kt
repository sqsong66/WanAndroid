package com.sqsong.wanandroid.ui.home.activity

import com.sqsong.wanandroid.R
import com.sqsong.wanandroid.common.inter.ChangeThemeAnnotation
import com.sqsong.wanandroid.data.KnowledgeData
import com.sqsong.wanandroid.ui.base.BaseActivity
import com.sqsong.wanandroid.ui.home.mvp.knowledge.ChildKnowledgePresenter
import com.sqsong.wanandroid.ui.home.mvp.knowledge.ChiledKnowledgeContract
import com.sqsong.wanandroid.util.Constants
import com.sqsong.wanandroid.util.SnackbarUtil
import com.sqsong.wanandroid.util.setupToolbar
import kotlinx.android.synthetic.main.activity_knowledge.*

@ChangeThemeAnnotation
class KnowledgeActivity : BaseActivity<ChildKnowledgePresenter>(), ChiledKnowledgeContract.View {

    override fun beforeInflateView() {
        val knowledgeData = intent.getParcelableExtra<KnowledgeData>(Constants.KNOWLEDGE_DATA)
        SnackbarUtil.showNormalToast(this, knowledgeData.name)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_knowledge
    }

    override fun initEvent() {
        setupToolbar(toolbar)
        mPresenter.onAttach(this)
    }

}
