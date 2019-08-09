package me.luowl.wan.ui.architecture

import me.luowl.wan.base.ArticlePageListViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.BaseResp
import me.luowl.wan.data.model.PageData

/*
 *
 * Created by luowl
 * Date: 2019/7/29 
 * Desc：
 */

class ArchitectureArticleListViewModel constructor(repository: WanRepository) : ArticlePageListViewModel(repository) {

    var treeId: Long = 0L

    override suspend fun request(): BaseResp<PageData> = repository.getArticleListByTreeId(pageIndex, treeId)
}