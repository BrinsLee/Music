package com.brins.musiclistlib.presenter

import com.brins.musiclistlib.contract.AlbumContract

class AlbumPresenter : AlbumContract.Presenter() {
    override suspend fun loadAlbumDetail(id: String) {
        val result = mModel?.loadAlbumDetail(id)
        mView?.onAlbumDetailLoad(result)
    }
}