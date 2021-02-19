package com.brins.loacl.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.brins.baselib.module.BaseMusic
import com.brins.loacl.callback.SingleMusicCallback
import com.brins.loacl.contarct.LocalContract

/**
 * Created by lipeilin
 * on 2021/2/18
 */
class LocalMusicViewModel private constructor(application: Application) :
    LocalContract.ViewModel(application) {

    private val mSingleMusicLiveData: MutableLiveData<MutableList<BaseMusic>> = MutableLiveData()

    fun getSingleMusicLiveData() = mSingleMusicLiveData

    companion object {
        private val TAG = "LocalMusicViewModel"

        @Volatile
        private var instance: LocalMusicViewModel? = null
        fun getInstance(application: Application): LocalMusicViewModel {
            if (instance != null) {
                return instance!!
            }
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = LocalMusicViewModel(application)
                    instance!!
                }
            }
        }
    }

    override fun loadLocalMusic(manager: LoaderManager) {
        mModel?.loadLocalMusic(manager, object : SingleMusicCallback() {

            override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
                val songs = ArrayList<BaseMusic>()
                if (cursor != null && cursor.count > 0) {
                    cursor.moveToFirst()
                    do {
                        val song = mModel?.cursorToMusic(cursor)
                        songs.add(song!!)
                    } while (cursor.moveToNext())
                }
                mSingleMusicLiveData.value = songs
            }

        })
    }
}