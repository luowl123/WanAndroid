package me.luowl.wan.ui.account

import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.*
import java.util.ArrayList

/*
 *
 * Created by luowl
 * Date: 2019/8/31 
 * Desc：
 */

class CoinViewModel constructor(val repository: WanRepository) :BaseViewModel(){
    private val _items = MutableLiveData<MutableList<CoinData>>().apply { value = mutableListOf() }
    val dataList: MutableLiveData<MutableList<CoinData>> = _items
    var pageIndex = 0
    var requestLoadDataState = MutableLiveData<Int>()

    override fun retry() {
        super.retry()
        pageIndex = 0
        getData()
    }

    fun getData() {
        launch({
            if (pageIndex == 0)
                startLoading()
            val resp = repository.getCoinList(pageIndex)
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
}