package com.brins.networklib.service

import com.brins.baselib.config.MUSIC_LIST
import com.brins.baselib.config.MUSIC_LIST.Companion.MUSIC_LIST_DAILY_RECOMMEND
import com.brins.baselib.config.MUSIC_LIST.Companion.MUSIC_LIST_INTELLIGENCE
import com.brins.baselib.config.MUSIC_LIST.Companion.MUSIC_LIST_USER_LIKE
import com.brins.baselib.module.like.UserLikeMusicResult
import com.brins.networklib.model.musiclist.MusicListIntelligenceResult
import com.brins.networklib.model.musiclist.MusicListResult
import com.brins.networklib.model.musiclist.MusicListsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicListService {

    @GET(MUSIC_LIST.MUSIC_LIST_DETAIL)
    fun getMusicUrl(@Query("id") id: String): Call<MusicListResult>

    @GET(MUSIC_LIST.MUSIC_USER_LIST)
    fun getUserMusicList(@Query("uid") id: String): Call<MusicListsResult>

    @GET(MUSIC_LIST_USER_LIKE)
    fun getUserLikeMusicList(@Query("uid") uid: String): Call<UserLikeMusicResult>

    @GET(MUSIC_LIST_DAILY_RECOMMEND)
    fun getDailyRecommendMusicList(): Call<MusicListsResult>

    @GET(MUSIC_LIST_INTELLIGENCE)
    fun getIntelligenceMusicList(
        @Query("id") id: String,
        @Query("pid") pid: String
    ): Call<MusicListIntelligenceResult>
}