package com.brins.mine.model

import com.brins.mine.contract.MineContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.event.EventsResult
import com.brins.networklib.model.musiclist.MusicListsResult

class MineModel : MineContract.Model {


    override suspend fun getMyMusicLists(id: String): MusicListsResult =
        ApiHelper.getMusicListService().getUserMusicList(id).await()

    override suspend fun getRecommendMusicList(): MusicListsResult =
        ApiHelper.getMusicListService().getDailyRecommendMusicList().await()

    override suspend fun getEventData(id: String): EventsResult =
        ApiHelper.getMineService().getMyEventData(id).await()

    override fun onDestroy() {

    }
}