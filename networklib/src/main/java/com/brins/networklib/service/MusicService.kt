package com.brins.networklib.service

import com.brins.baselib.config.MUSIC
import com.brins.baselib.config.MUSIC_COMMENT
import com.brins.networklib.model.comment.CommentResult
import com.brins.networklib.model.music.MusicLrcResult
import com.brins.networklib.model.music.MusicUrl
import com.brins.networklib.model.music.MusicUseable
import com.brins.networklib.model.musiclist.MoreMusicListResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {

    /**
     * 获取音乐链接
     *
     * @param id
     */
    @GET(MUSIC.MUSIC_URL)
    fun getMusicUrl(@Query("id") vararg id: String): Call<MusicUrl>

    /**
     * 音乐是否可用
     *
     * @param id
     * @return
     */
    @GET(MUSIC.MUSIC_USEABLE)
    fun getMusicUseable(@Query("id") id: String): Call<MusicUseable>

    /**
     * 获取歌词
     *
     * @param id
     * @return
     */
    @GET(MUSIC.MUSIC_LRC)
    fun getMusicLrc(@Query("id") id: String): Call<MusicLrcResult>

    /**
     * 获取音乐评论
     *
     * @param id
     * @param limit
     * @return
     */
    @GET(MUSIC_COMMENT.MUSIC_COMMENT)
    fun getMusicComment(
        @Query("id") id: String,
        @Query("limit") limit: Int = 20
    ): Call<CommentResult>

    @GET(MUSIC.MUSIC_DETAIL)
    fun getMusicDetail(@Query("ids") ids: String) : Call<MoreMusicListResult>

}