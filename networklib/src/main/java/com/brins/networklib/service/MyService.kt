package com.brins.networklib.service

import com.brins.baselib.config.MINE_INFO
import com.brins.baselib.config.MINE_MUSIC_LIST
import com.brins.networklib.model.event.EventsResult
import com.brins.networklib.model.follow.FollowResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2020/10/20
 */
interface MyService {

    @GET(MINE_MUSIC_LIST.MINE_MUSIC_LIST)
    fun getMyMusicLists(@Query("id") id: String)

    @GET(MINE_MUSIC_LIST.MINE_EVENT_DATA)
    fun getMyEventData(@Query("uid") uid: String): Call<EventsResult>

    @GET(MINE_INFO.MINE_INFO_FOLLOW)
    fun getMyFollows(@Query("uid") uid: String): Call<FollowResult>
}