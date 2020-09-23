package com.brins.musicdetail.presenter

import com.brins.musicdetail.contract.MusicDetailContract

class MusicDetailPresenter : MusicDetailContract.Presenter() {

    override suspend fun loadMusicLrc(id: String) {
        val result = mModel?.loadMusicLrc(id)
        mView?.onMusicLyricsLoad(result?.lrc)
    }
}