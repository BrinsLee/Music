package com.brins.find.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.find.contract.FindContract
import com.brins.networklib.model.album.AlbumResult
import com.brins.networklib.model.album.NewestAlbum
import com.brins.networklib.model.follow.FollowData
import com.brins.networklib.model.follow.FollowResult

/**
 * Created by lipeilin
 * on 2020/12/2
 */
class FindViewModel private constructor(application: Application) :
    FindContract.ViewModel(application) {


    private lateinit var mFollowData: FollowResult
    private val mFollowLiveData: MutableLiveData<MutableList<FollowData>> = MutableLiveData()

    fun getFollowData(): FollowResult = mFollowData
    fun getFollowLiveData(): MutableLiveData<MutableList<FollowData>> = mFollowLiveData

    companion object {

        @Volatile
        private var instance: FindViewModel? = null
        fun getInstance(application: Application): FindViewModel {
            if (instance != null) {
                return instance!!
            }
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = FindViewModel(application)
                    instance!!
                }
            }
        }
    }

    override suspend fun getMyFollows(uid: String) {
        val result = mModel?.loadMyFollows(uid)
        result?.let {
            mFollowData = it
            mFollowLiveData.value = it.follow
        }
    }


}