package me.luowl.wan.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/*
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
@Entity(tableName = "search_records")
class SearchRecord {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_id")
    var id: Long = 0L

    @ColumnInfo(name = "keyword")
    var keyword: String = ""

    @Ignore
    constructor()

    constructor(
        keyword: String = "",
        id: Long = 0L
    ) {
        this.id = id
        this.keyword = keyword
    }
}