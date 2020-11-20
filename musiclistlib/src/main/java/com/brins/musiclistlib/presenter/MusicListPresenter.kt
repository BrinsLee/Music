package com.brins.musiclistlib.presenter

import com.brins.musiclistlib.contract.MusicListContract
import java.lang.StringBuilder

class MusicListPresenter : MusicListContract.Presenter() {

    override suspend fun loadMusicListDetail(id: String) {
        val result = mModel?.loadMusicListDetail(id)
        mView?.onMusicDetailLoad(result)
    }


    override suspend fun loadMoreMusicListDetail(id: ArrayList<String>) {
        val builder = StringBuilder()
        for (i in id.indices) {
            builder.append(id[i])
            if (i != id.lastIndex) {
                builder.append(",")
            }
        }
        val result = mModel?.loadMoreMusicListDetail(builder.toString())
        mView?.onMoreMusicDetailLoad(result)
    }
}