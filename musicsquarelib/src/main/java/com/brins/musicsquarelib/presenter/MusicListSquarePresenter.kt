package com.brins.musicsquarelib.presenter

import com.brins.musicsquarelib.contract.MusicSquareContract

class MusicListSquarePresenter : MusicSquareContract.Presenter() {

    override suspend fun loadMusicList(cat: String) {
        val result = mModel?.loadHightQuality(cat)
        mView?.onMusicListLoad(result)
    }
}