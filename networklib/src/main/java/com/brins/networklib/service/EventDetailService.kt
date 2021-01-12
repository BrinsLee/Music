package com.brins.networklib.service

import com.brins.baselib.config.EVENT
import com.brins.networklib.model.comment.CommentResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2021/1/12
 */
interface EventDetailService {

    @GET(EVENT.EVENT_COMMENT)
    fun getEventComment(@Query("threadId") threadId: String) : Call<CommentResult>
}