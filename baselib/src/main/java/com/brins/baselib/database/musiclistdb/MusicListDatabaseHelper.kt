package com.brins.baselib.database.musiclistdb

import android.content.Context
import androidx.room.Room
import com.brins.baselib.module.MusicList
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by lipeilin
 * on 2020/10/27
 */
class MusicListDatabaseHelper(context: Context) {


    private val appDataBase =
        Room.databaseBuilder(context, MusicListDatabase::class.java, "dbRecommendMusicList")
            .build()

    fun insertMusicList(music: MusicList): Completable {
        return appDataBase.dao().addMusicList(music)
    }

    fun insertMusicList(music: MutableList<MusicList>): Completable {
        return appDataBase.dao().addMusicList(music)
    }

    fun getMusicList(): Single<List<MusicList>> {
        return appDataBase.dao().getMusicList()
    }

    fun deleteMusicList(id: String): Single<Int> {
        return appDataBase.dao().deleteMusicList(id)
    }

    fun deleteMusicLists(): Completable {
        return appDataBase.dao().deleteMusicLists()
    }
}