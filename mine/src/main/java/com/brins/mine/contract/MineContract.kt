package com.brins.mine.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.mine.model.MineModel
import com.brins.networklib.model.event.EventsResult
import com.brins.networklib.model.musiclist.MusicListsResult


interface MineContract {

    interface View : IView {

        fun onUserInfoLoad()

        fun onUserInfoUpdated()

    }

    abstract class Presenter : BasePresenter<MineModel, View>() {

        abstract fun fetchUserInfo()

        abstract fun updateUserInfo()

    }

    interface Model : IModel {

        suspend fun getMyMusicLists(id: String): MusicListsResult

        suspend fun getRecommendMusicList(): MusicListsResult

        suspend fun getEventData(id: String): EventsResult
    }

    abstract class ViewModel(application: Application) : BaseViewModel<MineModel>(application) {

        abstract suspend fun getMyMusicLists(id: String)

        abstract suspend fun getRecommendMusicLists()

        abstract suspend fun getSubmitAlbums()

        abstract suspend fun getEventData(id: String)
    }
}