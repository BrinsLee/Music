package com.brins.musiclistlib.presenter

import com.brins.musiclistlib.contract.MusicListContract

class MusicListPresenter : MusicListContract.Presenter() {

    override suspend fun loadMusicListDetail(id: String) {
        val result = mModel?.loadMusicListDetail(id)
        mView?.onMusicDetailLoad(result)
    }
}