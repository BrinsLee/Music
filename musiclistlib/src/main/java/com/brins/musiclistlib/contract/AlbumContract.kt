package com.brins.musiclistlib.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.musiclistlib.model.AlbumModel
import com.brins.networklib.model.album.AlbumListResult

interface AlbumContract {
    interface View : IView {

        fun onAlbumDetailLoad(data: AlbumListResult?)

    }

    abstract class Presenter : BasePresenter<AlbumModel, View>() {
        abstract suspend fun loadAlbumDetail(id: String)
    }

    interface Model : IModel {
        suspend fun loadAlbumDetail(id: String): AlbumListResult
    }
}