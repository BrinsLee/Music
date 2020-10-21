package com.brins.mine.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.mine.contract.MineContract
import com.brins.networklib.model.musiclist.MusicList
import com.brins.networklib.model.musiclist.MusicListsResult

class MineViewModel private constructor(application: Application) :
    MineContract.ViewModel(application) {

    private lateinit var mMusicListData: MusicListsResult
    private val mMusicListLiveData: MutableLiveData<MutableList<MusicList>> = MutableLiveData()

    fun getMutableMusicListData(): MutableLiveData<MutableList<MusicList>> = mMusicListLiveData

    fun getMusicListData(): MusicListsResult = mMusicListData

    companion object {

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
        }
    }
}