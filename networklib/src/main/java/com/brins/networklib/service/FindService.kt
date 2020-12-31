package com.brins.networklib.service

import com.brins.baselib.config.FIND
import com.brins.baselib.config.LIKE
import com.brins.networklib.model.event.EventsResult
import com.brins.networklib.model.like.LikeEventResult
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
        @Query("lasttime") lastTime: Long,
        @Query("pagesize") pageSize: Int
    ): Call<EventsResult>

    @GET(LIKE.LIKE_OR_UNLIKE)
    fun likeEvent(
        @Query("t") t: Int = 1,
        @Query("type") type: Int = 6,
        @Query("threadId") threadId: String
    ): Call<LikeEventResult>

    @GET(LIKE.LIKE_OR_UNLIKE)
    fun unlikeEvent(
        @Query("t") t: Int = 2,
        @Query("type") type: Int = 6,
        @Query("threadId") threadId: String
    ): Call<LikeEventResult>
}