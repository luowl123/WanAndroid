package me.luowl.wan.ui.home

import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.data.model.BannerData
import me.luowl.wan.util.GlobalUtil

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class HomeViewModel constructor(private val repository: WanRepository) : BaseViewModel() {

    private val _items = MutableLiveData<MutableList<ArticleData>>().apply { value = mutableListOf() }
    val dataList: MutableLiveData<MutableList<ArticleData>> = _items
    private var pageIndex = 0
    var requestLoadDataState = MutableLiveData<Int>()
    private val _bannerItems = MutableLiveData<MutableList<BannerData>>().apply { value = mutableListOf() }
    val bannerData: MutableLiveData<MutableList<BannerData>> = _bannerItems

    override fun retry() {
        super.retry()
        pageIndex = 0
        getData()
    }

    fun getData() {
        launch({
            val tempItems = ArrayList<ArticleData>()
            val bannerItems = ArrayList<BannerData>()
            if (pageIndex == 0) {
                startLoading()
                val bannerDataResp = repository.getBanner()
                bannerDataResp.data?.let {
                    bannerItems.addAll(it)
                }
                val topArticleResp = repository.getTopArticleList()
                topArticleResp.data?.let {
                    tempItems.addAll(it)
                }
            }
            val resp = repository.getHomeArticleList(pageIndex)
            if (pageIndex == 0) {
                _items.value?.clear()
                bannerData.value = bannerItems
            }
            when (resp.errorCode) {
                0 -> {
                    val pageData = resp.data ?: throw Throwable("page data is null")
                    val pageItems = java.util.ArrayList(pageData.datas ?: listOf())
                    if (pageIndex == 0 || _items.value == null) {
                        tempItems.addAll(pageItems)
                        _items.value = tempItems
                    } else {
                        _items.value?.addAll(pageItems)
                    }
//                    pageData.datas?.let {
//                        tempItems.addAll(it)
//                        if (_items.value == null) {
//                            _items.value = tempItems
//                        } else {
//                            _items.value?.addAll(tempItems)
//                        }
//                    }
                    if (pageIndex == 0 && bannerItems.size == 0) {
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
                }
                else -> {
                    if (pageIndex == 0 && bannerItems.size == 0) {
                        loadDataError()
                    } else {
                        requestLoadDataState.value = LoadMoreState.STATE_LOAD_FAIL
                        loadDataFinish()
                    }
                }
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
//            GlobalUtil.showToastShort("收藏成功")
        }, {
//            GlobalUtil.showToastShort("收藏失败")
        })
    }

    fun cancelCollectArticle(id: Long) {
        launch({
            val resp = repository.cancelCollectArticleByOriginId(id)
            checkResponseCode(resp)
//            GlobalUtil.showToastShort("取消收藏")
        }, {
//            GlobalUtil.showToastShort("操作失败")
        })
    }

}