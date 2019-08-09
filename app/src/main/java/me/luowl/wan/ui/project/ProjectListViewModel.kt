package me.luowl.wan.ui.project

import me.luowl.wan.base.ArticlePageListViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.BaseResp
import me.luowl.wan.data.model.PageData

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

class ProjectListViewModel(repository: WanRepository) : ArticlePageListViewModel(repository) {

    var treeId: Long = 0L

    override suspend fun request(): BaseResp<PageData> {
        return if (treeId == -1L) repository.getNewestProjectList(pageIndex) else repository.getProjectList(
            pageIndex,
            treeId
        )
    }

//    fun getData() {
//        launch({
//            if (pageIndex == 0)
//                stateModel.startLoading()
//            val resp = if (treeId == -1L) repository.getNewestProjectList(pageIndex) else repository.getProjectList(
//                pageIndex,
//                treeId
//            )
//            when (resp.errorCode) {
//                0 -> {
//                    val pageData = resp.data ?: throw Throwable("page data is null")
//                    val pageItems = ArrayList(pageData.datas ?: listOf())
//                    if (_items.value == null) {
//                        _items.value = pageItems
//                    } else {
//                        _items.value?.addAll(pageItems)
//                    }
//                    if (pageIndex == 0 && pageItems.size == 0) {
//                        stateModel.showEmpty()
//                    } else {
//                        stateModel.loadDataFinish()
//                    }
//                    if (pageIndex < pageData.pageCount - 1) {
//                        pageIndex++
//                        requestLoadDataState.value = LoadMoreState.STATE_LOAD_NONE
//                    } else {
//                        requestLoadDataState.value = LoadMoreState.STATE_LOAD_END
//                    }
//                }
//                else -> {
//                    if (pageIndex == 0) {
//                        stateModel.loadDataError()
//                    } else {
//                        requestLoadDataState.value = LoadMoreState.STATE_LOAD_FAIL
//                        stateModel.loadDataFinish()
//                    }
//                }
//            }
//        }, {
//            if (pageIndex == 0) {
//                stateModel.loadDataError()
//            } else {
//                requestLoadDataState.value = LoadMoreState.STATE_LOAD_FAIL
//                stateModel.loadDataFinish()
//            }
//        })
//    }
}