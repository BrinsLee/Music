package com.brins.musiclistlib.model

import com.brins.musiclistlib.contract.MusicListContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.musiclist.MoreMusicListResult
import com.brins.networklib.model.musiclist.MusicListResult

class MusicListModel : MusicListContract.Model {

    override suspend fun loadMusicListDetail(id: String): MusicListResult =
        ApiHelper.getMusicListService().getMusicUrl(id).await()

    override suspend fun loadMoreMusicListDetail(ids: String): MoreMusicListResult =
        ApiHelper.getMusicService().getMusicDetail(ids).await()


    override fun onDestroy() {
    }
}