package com.brins.home.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.home.contract.HomeContract
import com.brins.networklib.model.album.AlbumResult
import com.brins.networklib.model.album.NewestAlbum
import com.brins.networklib.model.music.TopMusic
import com.brins.networklib.model.music.TopMusicResult
import com.brins.networklib.model.personal.PersonalizedMusic
import com.brins.networklib.model.personal.PersonalizedResult

class HomeViewModel private constructor(application: Application) :
    HomeContract.ViewModel(application) {

    private lateinit var mAlbumData: AlbumResult<NewestAlbum>
    private val mAlbumLiveData: MutableLiveData<MutableList<NewestAlbum>> = MutableLiveData()

    private lateinit var mMusicData: TopMusicResult<TopMusic>
    private val mTopMusicLiveData: MutableLiveData<MutableList<TopMusic>> = MutableLiveData()

    private lateinit var mPersonalizedMusicData: PersonalizedResult<PersonalizedMusic>
    private val mPersonalizedMusicLiveData: MutableLiveData<MutableList<PersonalizedMusic>> =
        MutableLiveData()

    fun getMutableAlbumData(): MutableLiveData<MutableList<NewestAlbum>> = mAlbumLiveData

    fun getMutableTopMusicData(): MutableLiveData<MutableList<TopMusic>> = mTopMusicLiveData

    fun getMutablePersonalizedMusicData(): MutableLiveData<MutableList<PersonalizedMusic>> =
        mPersonalizedMusicLiveData

    fun getAlbumData() : AlbumResult<NewestAlbum> = mAlbumData

    fun getTopMusicData() : TopMusicResult<TopMusic> = mMusicData

    fun getPersonalizedMusicData() : PersonalizedResult<PersonalizedMusic> = mPersonalizedMusicData

    companion object {

        @Volatile
        private var instance: HomeViewModel? = null
        fun getInstance(application: Application): HomeViewModel {
            if (instance != null) {
                return instance!!
            }
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = HomeViewModel(application)
                    instance!!
                }
            }
        }
    }

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

    override suspend fun loadPersonalizedMusic() {
        val result = mModel?.loadPersonalizedMusic()
        result?.let {
            mPersonalizedMusicData = it
            mPersonalizedMusicLiveData.value = it.result
        }
    }
}