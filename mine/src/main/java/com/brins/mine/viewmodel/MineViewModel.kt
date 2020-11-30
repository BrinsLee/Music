package com.brins.mine.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.brins.baselib.cache.like.LikeCache
import com.brins.baselib.database.factory.DatabaseFactory
import com.brins.mine.contract.MineContract
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.subscribeDbResult
import com.brins.networklib.model.event.EventData
import com.brins.networklib.model.musiclist.MusicListsResult

class MineViewModel private constructor(application: Application) :
    MineContract.ViewModel(application) {

    private lateinit var mMusicListData: MusicListsResult
    private val mMusicListLiveData: MutableLiveData<MutableList<MusicList>> = MutableLiveData()

    fun getMutableMusicListData(): MutableLiveData<MutableList<MusicList>> = mMusicListLiveData

    fun getMusicListData(): MusicListsResult = mMusicListData

    private lateinit var mRecommendMusicListData: MusicListsResult
    private val mRecommendMusicListLiveData: MutableLiveData<MutableList<MusicList>> =
        MutableLiveData()

    fun getRecommendMutableMusicListData(): MutableLiveData<MutableList<MusicList>> =
        mRecommendMusicListLiveData

    fun getRecommendMusicListData(): MusicListsResult = mRecommendMusicListData

    private lateinit var mEventData: ArrayList<EventData>
    private val mEventLiveData: MutableLiveData<MutableList<EventData>> = MutableLiveData()

    fun getMutableEventData(): MutableLiveData<MutableList<EventData>> = mEventLiveData

    fun getEventData(): ArrayList<EventData> = mEventData


    companion object {

        val TYPE_MUSIC_LIST = 0
        val TYPE_RECOMMEND_MUSIC_LIST = 1

        @Volatile
        private var instance: MineViewModel? = null
        fun getInstance(application: Application): MineViewModel {
            if (instance != null) {
                return instance!!
            }
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = MineViewModel(application)
                    instance!!
                }
            }
        }
    }

    override suspend fun getMyMusicLists(id: String) {
        val result = mModel?.getMyMusicLists(id)
        result?.let {
            mMusicListData = it
            val list = mutableListOf<MusicList>()
            list.addAll(mMusicListData.playlists!!)
            mMusicListLiveData.value = list
            LikeCache.likeMusicList = list[0]
        }
    }

    override suspend fun getRecommendMusicLists() {
        val result = mModel?.getRecommendMusicList()
        result?.let {
            mRecommendMusicListData = it
            val list = mutableListOf<MusicList>()
            list.addAll(it.playlists!!)
            mRecommendMusicListLiveData.value = list
            DatabaseFactory.deleteMusicList().subscribeDbResult({
                DatabaseFactory.addMusicList(list).subscribeDbResult({
                    Log.d("Database", "$it")

                }, {
                    Log.d("Database", it.message ?: "000")

                })
            }, {
                Log.d("Database", it.message ?: "000")
            })

        }
    }

    override suspend fun getSubmitAlbums() {

    }

    override suspend fun getEventData(id: String) {
        val result = mModel?.getEventData(id)
        result?.let {
            if (it.events != null) {
                mEventData = it.events!!
                mEventLiveData.value = it.events
            }

        }
    }

    fun createDefaultRecommend(which: Int) {
        val list = mutableListOf<MusicList>()
        DatabaseFactory.getMusicList().subscribeDbResult({
            list.addAll(it)
            if (which == TYPE_MUSIC_LIST) {
                mMusicListLiveData.value = list
            } else {
                mRecommendMusicListLiveData.value = list
            }
            Log.d("Database", "getMusicList: $it")
        }, {
            val musicList = MusicList()
            musicList.apply {
                name = "请先登录"
            }
            list.add(musicList)
            if (which == TYPE_MUSIC_LIST) {
                mMusicListLiveData.value = list
            } else {
                mRecommendMusicListLiveData.value = list
            }
        })
    }
}