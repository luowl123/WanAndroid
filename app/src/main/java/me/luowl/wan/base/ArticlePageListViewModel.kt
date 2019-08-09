package me.luowl.wan.base

import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.data.model.BaseResp
import me.luowl.wan.data.model.PageData
import me.luowl.wan.util.GlobalUtil
import java.util.*

/*
 *
 * Created by luowl
 * Date: 2019/8/3 
 * Desc：
 */

abstract class ArticlePageListViewModel constructor(val repository: WanRepository) : BaseViewModel() {
    private val _items = MutableLiveData<MutableList<ArticleData>>().apply { value = mutableListOf() }
    val dataList: MutableLiveData<MutableList<ArticleData>> = _items
    var pageIndex = 0
    var requestLoadDataState = MutableLiveData<Int>()

    abstract suspend fun request(): BaseResp<PageData>

    override fun retry() {
        super.retry()
        pageIndex = 0
        getData()
    }

    fun getData() {
        launch({
            if (pageIndex == 0)
                startLoading()
            val resp = request()
            checkResponseCode(resp)
            val pageData = resp.data ?: throw Throwable("page data is null")
            val pageItems = ArrayList(pageData.datas ?: listOf())
            if (pageIndex == 0 || _items.value == null) {
                _items.value = pageItems
            } else {
                _items.value?.addAll(pageItems)
            }
            if (pageIndex == 0 && pageItems.size == 0) {
                loadDataEmpty()
            } else {
                loadDataFinish()
            }
            if (pageIndex < pageData.pageCount - 1) {
                pageIndex++
                requestLoadDataState.value = LoadMoreState.STATE_LOAD_NONE
            } else {
                requestLoadDataState.value = LoadMoreState.STATE_LOAD_END
            }
        }, {
            if (pageIndex == 0) {
                loadDataError()
            } else {
                requestLoadDataState.value = LoadMoreState.STATE_LOAD_FAIL
                loadDataFinish()
            }
        })
    }

    fun collectArticle(id: Long) {
        launch({
            val resp = repository.addCollect(id)
            checkResponseCode(resp)
            GlobalUtil.showToastShort("收藏成功")
        }, {
            GlobalUtil.showToastShort("收藏失败")
        })
    }

    fun cancelCollectArticle(id: Long) {
        launch({
            val resp = repository.cancelCollectArticleByOriginId(id)
            checkResponseCode(resp)
            GlobalUtil.showToastShort("取消收藏")
        }, {
            GlobalUtil.showToastShort("操作失败")
        })
    }

    fun cancelCollectArticle(id: Long, originId: Long) {
        launch({
            val resp = repository.cancelCollectArticle(id, originId)
            checkResponseCode(resp)
            GlobalUtil.showToastShort("取消收藏")
            retry()
        }, {
            GlobalUtil.showToastShort("操作失败")
        })
    }
}