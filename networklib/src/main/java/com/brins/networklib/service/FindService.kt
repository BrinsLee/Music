package com.brins.networklib.service

import com.brins.baselib.config.FIND
import com.brins.networklib.model.event.EventsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2020/12/22
 */
interface FindService {

    @GET(FIND.FIND_EVENT)
    fun getEvent(
        @Query("lasttime") lastTime: Int,
        @Query("pagesize") pageSize: Int
    ): Call<EventsResult>
}