package com.brins.networklib.service

import com.brins.baselib.config.MINE_MUSIC_LIST
import com.brins.networklib.model.event.EventsResult
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
}