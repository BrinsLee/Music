package com.brins.baselib.database.recentlypalydb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brins.baselib.module.BaseMusic

/**
 * Created by lipeilin
 * on 2020/10/19
 */
@Database(entities = arrayOf(BaseMusic::class), version = 1, exportSchema = false)
abstract class RecentlyMusicDatabase() : RoomDatabase() {

    abstract fun dao() : RecentlyPlayDao
}