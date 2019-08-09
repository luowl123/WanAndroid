package me.luowl.wan.ui.wxarticle

import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.Architecture

/*
 *
 * Created by luowl
 * Date: 2019/7/24 
 * Desc：
 */

class WXArticleChaptersViewModel constructor(private val repository: WanRepository) : BaseViewModel() {

    private val _items = MutableLiveData<MutableList<Architecture>>().apply { value = mutableListOf() }
    val dataList: MutableLiveData<MutableList<Architecture>> = _items

    fun getData() {
        launch {
            stateModel.startLoading()
            val resp = repository.getWXArticleChapters()
            checkResponseCode(resp)
            val data = resp.data ?: listOf()
            if (data.isNotEmpty()) {
                dataList.value = ArrayList(data)
                stateModel.loadDataFinish()
            } else {
                stateModel.showEmpty()
            }
        }
    }

}