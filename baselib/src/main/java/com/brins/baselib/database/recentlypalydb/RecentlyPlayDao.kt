package com.brins.baselib.database.recentlypalydb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brins.baselib.module.BaseMusic
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by lipeilin
 * on 2020/10/19
 */
@Dao
interface RecentlyPlayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecentlyMusic(music: BaseMusic): Completable

    @Query("select * from recently_music")
    fun getRecentlyMusic(): Single<List<BaseMusic>>

    @Query("delete from recently_music where ID=:id")
    fun deleteRecentlyMusic(id: String): Single<Int>
}