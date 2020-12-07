package com.brins.networklib.service

import com.brins.baselib.config.MUSIC
import com.brins.baselib.config.MUSIC_COMMENT
import com.brins.baselib.config.MUSIC_COMMENT.Companion.LIKE_UNLIKE_MUSIC_COMMENT
import com.brins.networklib.model.CommonResult
import com.brins.networklib.model.comment.CommentResult
import com.brins.networklib.model.like.LikeMusicResult
import com.brins.networklib.model.music.MusicLrcResult
import com.brins.networklib.model.music.MusicUrl
import com.brins.networklib.model.music.MusicUseable
import com.brins.networklib.model.musiclist.MoreMusicListResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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
    @GET(MUSIC_COMMENT.COMMENT)
    fun getComment(
        @Path("commentPath") path: String,
        @Query("id") id: String,
        @Query("limit") limit: Int = 20
    ): Call<CommentResult>

    /**
     * 获取音乐详情
     *
     * @param ids
     * @return
     */
    @GET(MUSIC.MUSIC_DETAIL)
    fun getMusicDetail(@Query("ids") ids: String): Call<MoreMusicListResult>

    /**
     * 喜欢/不喜欢音乐
     *
     * @param id
     * @param like
     * @return
     */
    @GET(MUSIC.MUSIC_LIKE)
    fun likeOrUnLikeMusic(
        @Query("id") id: String,
        @Query("like") like: Boolean = true
    ): Call<LikeMusicResult>

    /**
     * 点赞/取消点赞评论
     *
     * @param id
     * @param cid
     * @param t
     * @param type
     */
    @GET(LIKE_UNLIKE_MUSIC_COMMENT)
    fun likeOrUnLikeMusicComment(
        @Query("id") id: String,
        @Query("cid") cid: String,
        @Query("t") t: Int,
        @Query("type") type: Int
    ): Call<CommonResult>

}