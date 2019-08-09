package me.luowl.wan.ui.account

import me.luowl.wan.base.ArticlePageListViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.BaseResp
import me.luowl.wan.data.model.PageData

/*
 *
 * Created by luowl
 * Date: 2019/8/6 
 * Desc：
 */

class CollectionViewModel constructor(repository: WanRepository) : ArticlePageListViewModel(repository) {

    override suspend fun request(): BaseResp<PageData> = repository.getCollectionList(pageIndex)

}