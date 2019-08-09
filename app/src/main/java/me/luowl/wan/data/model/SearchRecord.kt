package me.luowl.wan.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
@Entity(tableName = "search_records")
data class SearchRecord @JvmOverloads constructor(
    @ColumnInfo(name = "keyword") var keyword: String = "",
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "record_id") var id:Long=0L
)