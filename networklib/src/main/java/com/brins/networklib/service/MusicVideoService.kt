package com.brins.networklib.service

import com.brins.baselib.config.VIDEO
import com.brins.networklib.model.musicvideo.LastestMvDataBean
import com.brins.networklib.model.musicvideo.MvCommentsResult
import com.brins.networklib.model.musicvideo.MvMetaResult
import com.brins.networklib.model.musicvideo.MvResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2021/1/29
 */
interface MusicVideoService {

    /**
     * 获取最新MV
     *
     * @param limit
     * @return
     */
    @GET(VIDEO.LASTEST_MUSIC_VIDEO)
    fun getLatestMusicVideo(@Query("limit") limit: Int): Call<MvResult>

    /**
     * 获取播放链接
     *
     * @param id
     * @return
     */
    @GET(VIDEO.MVURL)
    fun getMvMetaData(@Query("id") id: String): Call<MvMetaResult>

    /**
     * 获取MV详情
     *
     * @param id
     * @return
     */
    @GET(VIDEO.MVDETAIL)
    fun getMvDetail(@Query("id") id: String): Call<LastestMvDataBean>

    /**
     * 获取所有MV
     *
     * @param area
     * @param limit
     * @return
     */
    @GET(VIDEO.MVALL)
    fun getMvAll(@Query("area") area: String, @Query("limit") limit: Int): Call<MvResult>

    /**
     * 获取评论
     *
     * @param id
     * @return
     */
    @GET(VIDEO.MVCOMMENTS)
    fun getMvComments(@Query("id") id : String): Call<MvCommentsResult>

}