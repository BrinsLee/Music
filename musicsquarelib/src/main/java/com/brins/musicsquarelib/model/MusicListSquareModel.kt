package com.brins.musicsquarelib.model

import com.brins.musicsquarelib.contract.MusicSquareContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await

class MusicListSquareModel : MusicSquareContract.Model {

    override suspend fun loadHightQuality(cat : String) =
        ApiHelper.getPersonalizedService().getTopHightQuality(cat).await()

    override fun onDestroy() {

    }
}