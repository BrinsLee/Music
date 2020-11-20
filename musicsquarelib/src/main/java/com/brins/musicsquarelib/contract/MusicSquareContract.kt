package com.brins.musicsquarelib.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.musicsquarelib.model.MusicListSquareModel
import com.brins.networklib.model.musiclist.MusicListsResult

interface MusicSquareContract {

    interface View : IView {
        fun onMusicListLoad(result: MusicListsResult?)
    }

    abstract class Presenter : BasePresenter<MusicListSquareModel, View>() {
        abstract suspend fun loadMusicList(cat: String = "全部")
    }

    interface Model : IModel {
        suspend fun loadHightQuality(cat: String = "全部"): MusicListsResult
    }
}