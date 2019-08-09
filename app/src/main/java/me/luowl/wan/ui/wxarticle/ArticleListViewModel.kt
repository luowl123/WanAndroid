package me.luowl.wan.ui.wxarticle

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

class ArticleListViewModel(repository: WanRepository) : ArticlePageListViewModel(repository) {

    var chapterId: Long = 0L
    var keyword: String? = null

    override suspend fun request(): BaseResp<PageData> {
        return if (hasKeyWord()) {
            repository.getWXArticleListByKey(chapterId, pageIndex, keyword!!)
        } else {
            repository.getWXArticleList(chapterId, pageIndex)
        }
    }

    private fun hasKeyWord(): Boolean {
        return keyword != null && keyword!!.isNotEmpty()
    }

    fun reset() {
        pageIndex = 0
    }
}