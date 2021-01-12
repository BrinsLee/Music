package com.brins.eventdetaillib.model

import com.brins.eventdetaillib.contract.EventDetailContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await

/**
 * Created by lipeilin
 * on 2020/12/31
 */
class EventModel : EventDetailContract.Model {
    override suspend fun getEventComment(id: String) =
        ApiHelper.getEventDetailService().getEventComment(id)
            .await()

    override suspend fun getEventForward(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun likeOrUnLikeComment(id: String, cid: String, t: Int, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {

    }
}