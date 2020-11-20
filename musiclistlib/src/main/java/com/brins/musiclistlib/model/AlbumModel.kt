package com.brins.musiclistlib.model

import com.brins.musiclistlib.contract.AlbumContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.album.AlbumListResult

class AlbumModel : AlbumContract.Model {

    override suspend fun loadAlbumDetail(id: String): AlbumListResult {
        return ApiHelper.getAlbumService().getAlbumDetail(id).await()
    }

    override fun onDestroy() {
    }
}