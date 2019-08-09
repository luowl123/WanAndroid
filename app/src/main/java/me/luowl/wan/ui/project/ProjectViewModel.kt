package me.luowl.wan.ui.project

import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.Architecture

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

class ProjectViewModel constructor(private val repository: WanRepository) : BaseViewModel() {

    private val _items = MutableLiveData<MutableList<Architecture>>().apply { value = mutableListOf() }
    val dataList: MutableLiveData<MutableList<Architecture>> = _items

    fun getData() {
        launch({
            stateModel.startLoading()
            val resp = repository.getProjectClassification()
            val tempItems = ArrayList<Architecture>()
            val newProjectItem = Architecture()
            newProjectItem.id = -1
            newProjectItem.name = "最新"
            tempItems.add(newProjectItem)
            resp.data?.let {
                if (it.isNotEmpty()) {
                    tempItems.addAll(it)
                }
            }
            _items.value = tempItems
            stateModel.loadDataFinish()
        }, {
            stateModel.loadDataError()
        })
    }
}