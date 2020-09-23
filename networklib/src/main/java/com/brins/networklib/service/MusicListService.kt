package com.brins.networklib.service

import com.brins.baselib.config.MUSIC_LIST
import com.brins.networklib.model.music.MusicUrl
import com.brins.networklib.model.musiclist.MusicListResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicListService {

    @GET(MUSIC_LIST.MUSIC_LIST_DETAIL)
    fun getMusicUrl(@Query("id") id: String): Call<MusicListResult>
}