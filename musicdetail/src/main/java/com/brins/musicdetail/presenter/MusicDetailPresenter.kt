package com.brins.musicdetail.presenter

import com.brins.musicdetail.contract.MusicDetailContract

class MusicDetailPresenter : MusicDetailContract.Presenter() {

    override suspend fun loadMusicLrc(id: String) {
        val result = mModel?.loadMusicLrc(id)
        mView?.onMusicLyricsLoad(result?.lrc)
    }

    override suspend fun likeMusic(id: String) {
        val result = mModel?.likeMusic(id)
        result?.let {
            mView?.onLikeMusic(it.code == 200, id)
        }
    }

    override suspend fun unLikeMusic(id: String) {
        val result = mModel?.UnLikeMusic(id)
        result?.let {
            mView?.onDislikeMusic(it.code == 200, id)
        }
    }


}