package com.brins.networklib.service

import com.brins.baselib.config.MINE_MUSIC_LIST
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2020/10/20
 */
interface MyService {

    @GET(MINE_MUSIC_LIST.MINE_MUSIC_LIST)
    suspend fun getMyMusicLists(@Query("id") id : String)
}