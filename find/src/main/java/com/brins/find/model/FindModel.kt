package com.brins.find.model

import com.brins.find.contract.FindContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.event.EventsResult
import com.brins.networklib.model.follow.FollowResult

/**
 * Created by lipeilin
 * on 2020/12/1
 */
class FindModel : FindContract.Model {

    override suspend fun loadUserEvent(lastTime: Long, pageSize: Int): EventsResult =
        ApiHelper.getFindService().getEvent(lastTime, pageSize).await()


    override suspend fun loadMyFollows(uid: String): FollowResult =
        ApiHelper.getMineService().getMyFollows(uid).await()

    override fun onDestroy() {
    }
}