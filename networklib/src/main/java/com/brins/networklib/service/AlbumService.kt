package com.brins.networklib.service

import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_ALBUM_DETAIL
import com.brins.networklib.model.album.AlbumListResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumService {

    @GET(RECOMMEND_ALBUM_DETAIL)
    fun getAlbumDetail(@Query("id") id: String): Call<AlbumListResult>
}