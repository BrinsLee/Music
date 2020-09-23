package com.brins.musicsquarelib.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.musicsquarelib.model.MusicListSquareModel

interface MusicSquareContract {

    interface View : IView {
        fun onMusicListLoad()
    }

    abstract class Presenter : BasePresenter<MusicListSquareModel, View>() {
        abstract suspend fun loadMusicList(id: String)
    }

    interface Model : IModel {

    }
}