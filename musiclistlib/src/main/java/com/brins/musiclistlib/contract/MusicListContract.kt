package com.brins.musiclistlib.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.musiclistlib.model.MusicListModel
import com.brins.networklib.model.musiclist.MusicListResult

interface MusicListContract {

    interface View : IView {

        fun onMusicDetailLoad(data: MusicListResult?)
    }

    abstract class Presenter : BasePresenter<MusicListModel, View>() {
        abstract suspend fun loadMusicListDetail(id: String)
    }

    interface Model : IModel {
        suspend fun loadMusicListDetail(id: String): MusicListResult
    }
}