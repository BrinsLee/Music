package com.brins.video.model

import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.musicvideo.MvCommentsResult
import com.brins.networklib.model.musicvideo.MvMetaResult
import com.brins.networklib.model.musicvideo.MvResult
import com.brins.video.contract.VideoContract

/**
 * Created by lipeilin
 * on 2021/1/29
 */
class VideoModel : VideoContract.Model {

    override suspend fun loadVideoData(limit: Int, area: String): MvResult =
        ApiHelper.getMusicVideoService().getMvAll(area, limit).await()

    override suspend fun loadUrl(id: String): MvMetaResult =
        ApiHelper.getMusicVideoService().getMvMetaData(id).await()

    override suspend fun loadVideoComments(id: String): MvCommentsResult =
        ApiHelper.getMusicVideoService().getMvComments(id).await()

    override fun onDestroy() {
    }
}