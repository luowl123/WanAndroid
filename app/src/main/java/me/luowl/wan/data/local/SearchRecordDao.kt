package me.luowl.wan.data.local

import androidx.room.*
import me.luowl.wan.data.model.SearchRecord

/*
 *
 * Created by luowl
 * Date: 2019/7/30 
 * Desc：
 */
@Dao
interface SearchRecordDao {
    @Query("SELECT * FROM search_records order by record_id desc")
    suspend fun getRecords(): List<SearchRecord>

    @Insert
    suspend fun insertRecord(record: SearchRecord)

    @Delete
    suspend fun deleteRecord(record: SearchRecord)

    @Query("SELECT * FROM search_records WHERE keyword=:keyword")
    suspend fun findRecord(keyword: String): List<SearchRecord>

    @Query("DELETE FROM search_records WHERE keyword=:keyword")
    suspend fun deleteRecords(keyword: String)
}