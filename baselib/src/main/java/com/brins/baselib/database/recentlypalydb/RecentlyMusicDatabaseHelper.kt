package com.brins.baselib.database.recentlypalydb

import android.content.Context
import androidx.room.Room
import com.brins.baselib.module.BaseMusic
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class RecentlyMusicDatabaseHelper(context: Context) {

    private val appDataBase =
        Room.databaseBuilder(context, RecentlyMusicDatabase::class.java, "dbRecentlyMusic")
            .build()

    fun insertRecentlyMusic(music: BaseMusic): Completable {
        return appDataBase.dao().addRecentlyMusic(music)
    }

    fun getRecentlyMusic(): Single<List<BaseMusic>> {
        return appDataBase.dao().getRecentlyMusic()
    }

    fun deleteRecentlyMusic(id: String): Single<Int> {
        return appDataBase.dao().deleteRecentlyMusic(id)
    }
}