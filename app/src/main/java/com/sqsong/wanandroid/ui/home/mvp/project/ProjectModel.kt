package com.sqsong.wanandroid.ui.home.mvp.project

import com.sqsong.wanandroid.data.HomeItemBean
import com.sqsong.wanandroid.data.KnowledgeBean
import com.sqsong.wanandroid.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class ProjectModel @Inject constructor(private val apiService: ApiService) : ProjectContract.Model {
    override fun getProjectClassify(): Observable<KnowledgeBean> = apiService.getProjectClassify()

    override fun getProjectList(page: Int, cid: Int): Observable<HomeItemBean> = apiService.getProjectList(page, cid)

    override fun onDestroy() {

    }
}