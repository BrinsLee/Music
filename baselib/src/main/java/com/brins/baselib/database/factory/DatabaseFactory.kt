package com.brins.baselib.database.factory

import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.database.musiclistdb.MusicListDatabaseHelper
import com.brins.baselib.database.recentlypalydb.RecentlyMusicDatabaseHelper
import com.brins.baselib.database.userinfodb.UserInfoDatabaseHelper
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.MusicList
import com.brins.baselib.module.userlogin.UserAccountBean
import com.brins.baselib.module.userlogin.UserProfileBean
import com.brins.baselib.utils.UIUtils
import io.reactivex.Completable
import io.reactivex.Single

object DatabaseFactory {

    private var mRecentlyMusicDB: RecentlyMusicDatabaseHelper? = null
    private var mUserInfoDB: UserInfoDatabaseHelper? = null
    private var mMusicListDB: MusicListDatabaseHelper? = null


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

    private fun getUserInfoDB(): UserInfoDatabaseHelper {
        if (mUserInfoDB == null) {
            synchronized(UserInfoDatabaseHelper::class.java) {
                if (mUserInfoDB == null) {
                    mUserInfoDB = UserInfoDatabaseHelper(UIUtils.getContext())
                }
            }
        }
        return mUserInfoDB!!
    }

    private fun getMusicListDB(): MusicListDatabaseHelper {
        if (mMusicListDB == null) {
            synchronized(MusicListDatabaseHelper::class.java) {
                if (mMusicListDB == null) {
                    mMusicListDB = MusicListDatabaseHelper(UIUtils.getContext())
                }
            }
        }
        return mMusicListDB!!
    }

    //用户登录
    fun storeUserInfo(result: UserAccountBean): Single<Long> {
        return getUserInfoDB().insertUserAccount(result)
    }

    fun storeUserProfile(profile: UserProfileBean): Single<Long> {
        return getUserInfoDB().insertUserProfile(profile)
    }

    fun getUserInfo(): Single<UserAccountBean> {
        return getUserInfoDB().getUserAccount()
    }

    fun getUserProfile(): Single<UserProfileBean> {
        return getUserInfoDB().getUserProfile()
    }

    fun deleteUserInfo(): Single<Int>? {

        LoginCache.userAccount?.let {
            return getUserInfoDB().deleteUserAccount(it)
        }
        return null
    }

    fun deleteUserProfile(): Single<Int>? {

        LoginCache.userProfile?.let {
            return getUserInfoDB().deleteUserProfile(it)
        }
        return null
    }

    //最近播放
    fun addRecentlyMusic(music: BaseMusic): Completable {
        return getRecentlyMusicDB().insertRecentlyMusic(music)
    }

    fun getRecentlyMusics(): Single<List<BaseMusic>> {
        return getRecentlyMusicDB().getRecentlyMusic()
    }

    //推荐歌单
    fun addMusicList(musicList: MusicList): Completable {
        return getMusicListDB().insertMusicList(musicList)
    }

    fun addMusicList(musicList: MutableList<MusicList>): Completable {
        return getMusicListDB().insertMusicList(musicList)
    }

    fun getMusicList(): Single<List<MusicList>> {
        return getMusicListDB().getMusicList()
    }

    fun deleteMusicList(): Completable {

        return getMusicListDB().deleteMusicLists()
    }
}