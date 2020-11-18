package com.brins.searchlib.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.brins.baselib.config.SEARCH
import com.brins.baselib.module.*
import com.brins.baselib.utils.TUtil
import com.brins.networklib.model.search.SearchResult
import com.brins.searchlib.contract.SearchContract

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class SearchViewModel private constructor(application: Application) :
    SearchContract.ViewModel(application) {

    companion object {

        private val TAG = "SearchViewModel"

        @Volatile
        private var instance: SearchViewModel? = null
        fun getInstance(application: Application): SearchViewModel {
            if (instance != null) {
                return instance!!
            }
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = SearchViewModel(application)
                    instance!!
                }
            }
        }
    }

    private lateinit var mMusicData: ArrayList<BaseMusic>
    private val mMusicLiveData: MutableLiveData<SearchResult<BaseMusic>> = MutableLiveData()

    fun getMutableMusicLiveData(): MutableLiveData<SearchResult<BaseMusic>> = mMusicLiveData
    fun getMusicData(): ArrayList<BaseMusic> = mMusicData

    private lateinit var mAlbumData: ArrayList<BaseMusic.Album>
    private val mAlbumLiveData: MutableLiveData<SearchResult<BaseMusic.Album>> = MutableLiveData()

    fun getMutableAlbumLiveData(): MutableLiveData<SearchResult<BaseMusic.Album>> = mAlbumLiveData
    fun getAlbumData(): ArrayList<BaseMusic.Album> = mAlbumData

    private lateinit var mArtistData: ArrayList<BaseMusic.Artist>
    private val mArtistLiveData: MutableLiveData<SearchResult<BaseMusic.Artist>> = MutableLiveData()

    fun getMutableArtistLiveData(): MutableLiveData<SearchResult<BaseMusic.Artist>> =
        mArtistLiveData

    fun getArtistData(): ArrayList<BaseMusic.Artist> = mArtistData

    private lateinit var mMusicListData: ArrayList<MusicList>
    private val mMusicListLiveData: MutableLiveData<SearchResult<MusicList>> = MutableLiveData()

    fun getMutableMusicListLiveData(): MutableLiveData<SearchResult<MusicList>> =
        mMusicListLiveData

    fun getMusicListData(): ArrayList<MusicList> = mMusicListData

    private lateinit var mUserData: ArrayList<UserProfile>
    private val mUserLiveData: MutableLiveData<SearchResult<UserProfile>> = MutableLiveData()

    fun getMutableUserLiveData(): MutableLiveData<SearchResult<UserProfile>> =
        mUserLiveData

    fun getUserData(): ArrayList<UserProfile> = mUserData

    private lateinit var mMusicVideoData: ArrayList<BaseMusicVideo>
    private val mMusicVideoLiveData: MutableLiveData<SearchResult<BaseMusicVideo>> =
        MutableLiveData()

    fun getMutableMusicVideoLiveData(): MutableLiveData<SearchResult<BaseMusicVideo>> =
        mMusicVideoLiveData

    fun getMusicVideoData(): ArrayList<BaseMusicVideo> = mMusicVideoData

    private lateinit var mRadioData: ArrayList<DjRadio>
    private val mRadioLiveData: MutableLiveData<SearchResult<DjRadio>> =
        MutableLiveData()

    fun getMutableRadioLiveData(): MutableLiveData<SearchResult<DjRadio>> =
        mRadioLiveData

    fun getRadioData(): ArrayList<DjRadio> = mRadioData

    override suspend fun loadSearchResult(input: String, type: Int) {
        Log.d(TAG, "调用了loadSearchResult")
        if (mModel == null) {
            mModel = TUtil.getSuperT(this, 0)
        }
        when (type) {
            SEARCH.TYPE_MUSIC -> {
                val result = mModel?.loadMusicSearchResult(
                    input, type
                )
                result?.let {
                    mMusicData = it.dataBean?.data!!
                    mMusicLiveData.value = it
                }

            }
            SEARCH.TYPE_ALBUM -> {
                val result = mModel?.loadAlbumSearchResult(
                    input, type
                )
                result?.let {
                    mAlbumData = it.dataBean?.data!!
                    mAlbumLiveData.value = it
                }

            }
            SEARCH.TYPE_ARTIST -> {
                val result = mModel?.loadArtistSearchResult(
                    input, type
                )
                result?.let {
                    mArtistData = it.dataBean?.data!!
                    mArtistLiveData.value = it
                }

            }
            SEARCH.TYPE_MUSIC_LIST -> {
                val result = mModel?.loadMusicListSearchResult(
                    input, type
                )
                result?.let {
                    mMusicListData = it.dataBean?.data!!
                    mMusicListLiveData.value = it
                }

            }
            SEARCH.TYPE_USER -> {
                val result = mModel?.loadUserSearchResult(
                    input, type
                )
                result?.let {
                    mUserData = it.dataBean?.data!!
                    mUserLiveData.value = it
                }

            }
            SEARCH.TYPE_MV -> {
                val result = mModel?.loadMusicVideoSearchResult(
                    input, type
                )
                result?.let {
                    mMusicVideoData = it.dataBean?.data!!
                    mMusicVideoLiveData.value = it
                }

            }
            SEARCH.TYPE_RADIO -> {
                val result = mModel?.loadRadioSearchResult(
                    input, type
                )
                result?.let {
                    mRadioData = it.dataBean?.data!!
                    mRadioLiveData.value = it
                }

            }
        }
    }
}