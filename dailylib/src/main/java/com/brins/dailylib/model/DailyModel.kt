package com.brins.dailylib.model

import com.brins.dailylib.contract.DailyContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.daily.DailyMusicResult

/**
 * Created by lipeilin
 * on 2020/11/1
 */
class DailyModel : DailyContract.Model {
    override fun onDestroy() {

    }

    override suspend fun getDailyMusic(): DailyMusicResult =
        ApiHelper.getPersonalizedService().getDailyMusicRecommend().await()

}