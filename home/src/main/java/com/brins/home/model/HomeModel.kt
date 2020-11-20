package com.brins.home.model

import com.brins.home.contract.HomeContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.music.TopMusic
import com.brins.networklib.model.music.TopMusicResult
import com.brins.baselib.module.MusicList
import com.brins.networklib.model.musiclist.MusicListIntelligenceResult
import com.brins.networklib.model.recommend.RecommendResult

class HomeModel : HomeContract.Model {

    override suspend fun loadPersonalizedMusicList() =
        ApiHelper.getPersonalizedService().getPersonalizedMusicList().await()

    override suspend fun loadPersonalizedMusic() =
        ApiHelper.getPersonalizedService().getPersonalizedNewMusic().await()

    override suspend fun loadBanner() = ApiHelper.getPersonalizedService().getBanner().await()

    override suspend fun loadNewestAlbum() =
        ApiHelper.getPersonalizedService().getRecommendNewestAlbum().await()

    override suspend fun loadTopMusic(type: Int): TopMusicResult<TopMusic> =
        ApiHelper.getPersonalizedService().getRecommendTopSong(type).await()

    override fun onDestroy() {
    }

    override suspend fun loadHotOrNewRecommend(type: String): RecommendResult<MusicList>? =
        ApiHelper.getPersonalizedService().getTopRecommendw(order = type).await()

    override suspend fun loadIntelligenceMusicList(
        id: String,
        pid: String
    ): MusicListIntelligenceResult? =
        ApiHelper.getMusicListService().getIntelligenceMusicList(id, pid).await()

}