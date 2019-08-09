package me.luowl.wan.ui.architecture

import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.Architecture

/*
 *
 * Created by luowl
 * Date: 2019/7/27 
 * Desc：
 */

class ArchitectureViewModel constructor(private val repository: WanRepository) : BaseViewModel() {


    private val _items = MutableLiveData<MutableList<Architecture>>().apply { value = mutableListOf() }
    val dataList: MutableLiveData<MutableList<Architecture>> = _items

    var leftTabIndex = MutableLiveData<Int>()

    fun getData() {
        launch({
            stateModel.startLoading()
            val resp = repository.getArchitectureTree()
            val data = resp.data ?: listOf()
            if (data.isNotEmpty()) {
                _items.value = ArrayList(data)
                leftTabIndex.value = 0
                stateModel.loadDataFinish()
            } else {
                stateModel.showEmpty()
            }
        }, {
            stateModel.loadDataError()
        })
    }
}