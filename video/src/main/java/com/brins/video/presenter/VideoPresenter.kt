package com.brins.video.presenter

import com.brins.networklib.model.musicvideo.Mv
import com.brins.video.contract.VideoContract

/**
 * Created by lipeilin
 * on 2021/1/29
 */
class VideoPresenter : VideoContract.Presenter() {

    override suspend fun loadVideoData(limit: Int, area: String) {
        val result = mModel?.loadVideoData(limit, area)
        result?.let {
            val videoList = mutableListOf<Mv>()
            result.dataBeans?.forEach {
                val url = mModel!!.loadUrl(it.id)
                if (url.dataBean != null) {
                    videoList.add(Mv(it, url.dataBean!!))
                }
            }
            mView?.onMusicVideoLoad(videoList)
        }
    }


    override suspend fun loadVideoComments(id: String) {
        val result = mModel?.loadVideoComments(id)
        result?.let {
            mView?.onMusicVideoCommentLoad(it)
        }
    }
}