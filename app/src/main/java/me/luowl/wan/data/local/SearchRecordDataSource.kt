package me.luowl.wan.data.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.luowl.wan.data.model.SearchRecord

/*
 *
 * Created by luowl
 * Date: 2019/7/30 
 * Desc：
 */

class SearchRecordDataSource internal constructor(
    private val recordDao: SearchRecordDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getRecords(): List<SearchRecord> = withContext(ioDispatcher) {
        recordDao.getRecords()
    }

    suspend fun insertRecord(record: SearchRecord) = withContext(ioDispatcher) {
        recordDao.insertRecord(record)
    }

    suspend fun deleteRecord(record: SearchRecord) = withContext(ioDispatcher) {
        recordDao.deleteRecord(record)
    }

    suspend fun findRecord(keyword: String): List<SearchRecord> = withContext(ioDispatcher) {
        recordDao.findRecord(keyword)
    }

    suspend fun deleteRecords(keyword: String)= withContext(ioDispatcher){
        recordDao.deleteRecords(keyword)
    }
}