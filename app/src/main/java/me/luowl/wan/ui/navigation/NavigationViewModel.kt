package me.luowl.wan.ui.navigation

import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.model.Navigation

/*
 *
 * Created by luowl
 * Date: 2019/7/27 
 * Desc：
 */

class NavigationViewModel constructor(private val repository: WanRepository) : BaseViewModel() {

    private val _items = MutableLiveData<List<Navigation>>().apply { value = listOf() }
    val dataList: MutableLiveData<List<Navigation>> = _items
    val leftTabIndex = MutableLiveData<Int>()

    fun getData() {
        launch({
            stateModel.startLoading()
            val resp = repository.getNavigationData()
            val data = resp.data ?: listOf()
            if (data.isNotEmpty()) {
                _items.value = data
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