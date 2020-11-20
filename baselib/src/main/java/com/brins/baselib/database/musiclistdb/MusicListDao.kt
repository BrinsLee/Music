package com.brins.baselib.database.musiclistdb

import androidx.room.*
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.MusicList
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by lipeilin
 * on 2020/10/27
 */
@Dao
interface MusicListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMusicList(musicList: MusicList): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun addMusicList(music: MutableList<MusicList>): Completable

    @Query("select * from recommend_music_list")
    fun getMusicList(): Single<List<MusicList>>

    @Query("delete from recommend_music_list where ID=:id")
    fun deleteMusicList(id: String): Single<Int>

    @Query("delete from recommend_music_list")
    fun deleteMusicLists(): Completable
}