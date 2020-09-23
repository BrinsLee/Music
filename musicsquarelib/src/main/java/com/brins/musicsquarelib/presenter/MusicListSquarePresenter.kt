package com.brins.musicsquarelib.presenter

import com.brins.musicsquarelib.contract.MusicSquareContract

class MusicListSquarePresenter : MusicSquareContract.Presenter() {

    override suspend fun loadMusicList() {
        val result = mModel?.loadHightQuality()
        mView?.onMusicListLoad(result)
    }
}