package com.brins.mine.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.mine.model.MineModel
import com.brins.networklib.model.musiclist.MusicListsResult


interface MineContract {

    interface View : IView {


    }

    abstract class Presenter : BasePresenter<MineModel, View>() {


    }

    interface Model : IModel {

        suspend fun getMyMusicLists(id: String) : MusicListsResult

        suspend fun getRecommendMusicList() : MusicListsResult
    }

    abstract class ViewModel(application: Application) : BaseViewModel<MineModel>(application) {

        abstract suspend fun getMyMusicLists(id: String)

        abstract suspend fun getRecommendMusicLists()
    }
}