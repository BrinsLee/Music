package com.brins.baselib.database.musiclistdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brins.baselib.module.MusicList

/**
 * Created by lipeilin
 * on 2020/10/27
 */
@Database(entities = arrayOf(MusicList::class), version = 1, exportSchema = false)
abstract class MusicListDatabase : RoomDatabase() {

    abstract fun dao() : MusicListDao

}