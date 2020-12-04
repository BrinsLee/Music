package com.brins.find.model

import com.brins.find.contract.FindContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.follow.FollowResult

/**
 * Created by lipeilin
 * on 2020/12/1
 */
class FindModel : FindContract.Model {
    override suspend fun loadUserEvent() {
        TODO("Not yet implemented")
    }

    override suspend fun loadMyFollows(uid: String): FollowResult =
        ApiHelper.getMineService().getMyFollows(uid).await()

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}