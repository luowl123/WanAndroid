package me.luowl.wan.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.luowl.wan.base.ArticlePageListViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.data.local.SearchRecordDataSource
import me.luowl.wan.data.model.BaseResp
import me.luowl.wan.data.model.PageData
import me.luowl.wan.data.model.SearchRecord

/*
 *
 * Created by luowl
 * Date: 2019/7/29 
 * Desc：
 */

class SearchViewModel constructor(
    repository: WanRepository,
    private val localDataSource: SearchRecordDataSource
) : ArticlePageListViewModel(repository) {

    private val _records = MutableLiveData<List<SearchRecord>>().apply { value = mutableListOf() }
    val records: LiveData<List<SearchRecord>> = _records

    var keyword: String? = null

    fun queryRecord() {
        launch {
            _records.value = localDataSource.getRecords()
        }
    }

    fun deleteRecord(keyword: String) {
        launch {
            localDataSource.deleteRecords(keyword)
            queryRecord()
        }
    }

    private fun insertRecord(keyword: String) {
        launch({
            val oldRecords = localDataSource.findRecord(keyword)
            if (oldRecords.isNotEmpty()) {
                localDataSource.deleteRecords(keyword)
            }
            val newRecord = SearchRecord(keyword)
            localDataSource.insertRecord(newRecord)
        }, {})
    }

    fun doSearch(key: String) {
        insertRecord(key)
        keyword = key
        retry()
    }

    override suspend fun request(): BaseResp<PageData> = repository.searchArticleByKey(pageIndex, keyword!!)

    fun searchArticleByKey() {
        getData()
    }

}