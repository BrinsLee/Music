package com.brins.baselib.database.factory

import com.brins.baselib.database.recentlypalydb.RecentlyMusicDatabaseHelper
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.utils.UIUtils
import io.reactivex.Completable
import io.reactivex.Single

object DataBaseFactory {

    private var mRecentlyMusicDB: RecentlyMusicDatabaseHelper? = null

    private fun getRecentlyMusicDB(): RecentlyMusicDatabaseHelper {
        if (mRecentlyMusicDB == null) {
            synchronized(RecentlyMusicDatabaseHelper::class.java) {
                if (mRecentlyMusicDB == null) {
                    mRecentlyMusicDB = RecentlyMusicDatabaseHelper(UIUtils.getContext())
                }
            }
        }
        return mRecentlyMusicDB!!
    }

    fun addRecentlyMusic(music: BaseMusic): Completable {
        return getRecentlyMusicDB().insertRecentlyMusic(music)
    }

    fun getRecentlyMusics(): Single<List<BaseMusic>> {
        return getRecentlyMusicDB().getRecentlyMusic()
    }
}