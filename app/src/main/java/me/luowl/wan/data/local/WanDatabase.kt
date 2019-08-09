package me.luowl.wan.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.luowl.wan.WanApplication
import me.luowl.wan.data.model.SearchRecord

/*
 *
 * Created by luowl
 * Date: 2019/7/30 
 * Desc：
 */
@Database(entities = [SearchRecord::class], version = 2, exportSchema = false)
abstract
class WanDatabase : RoomDatabase() {
    abstract fun searchRecordDao(): SearchRecordDao

    companion object {

        val instance = Single.sin

    }

    private object Single {

        val sin: WanDatabase = Room.databaseBuilder(
            WanApplication.context,
            WanDatabase::class.java,
            "Wan.db"
        )
            .allowMainThreadQueries()
            .build()
    }

}