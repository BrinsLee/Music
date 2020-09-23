package com.brins.home.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.home.contract.HomeContract
import com.brins.networklib.model.album.AlbumResult
import com.brins.networklib.model.album.NewestAlbum
import com.brins.networklib.model.music.TopMusic
import com.brins.networklib.model.music.TopMusicResult

class HomeViewModel(application: Application) : HomeContract.ViewModel(application) {

    private lateinit var mAlbumData: AlbumResult<NewestAlbum>
    private val mAlbumLiveData: MutableLiveData<MutableList<NewestAlbum>> = MutableLiveData()

    private lateinit var mMusicData: TopMusicResult<TopMusic>
    private val mTopMusicLiveData: MutableLiveData<MutableList<TopMusic>> = MutableLiveData()

    fun getMutableAlbumData(): MutableLiveData<MutableList<NewestAlbum>> = mAlbumLiveData

    fun getMutableTopMusicData(): MutableLiveData<MutableList<TopMusic>> = mTopMusicLiveData


    override suspend fun loadNewestAlbum() {
        val result = mModel?.loadNewestAlbum()
        result?.let {
            mAlbumData = it
            mAlbumLiveData.value = mAlbumData.albums
        }
    }

    override suspend fun loadTopMusic(type: Int) {
        val result = mModel?.loadTopMusic(type)
        result?.let {
            mMusicData = it
            mTopMusicLiveData.value = it.data
        }
    }
}