package com.brins.networklib.service


import com.brins.baselib.config.BANNER.Companion.BANNER
import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_ALBUM
import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_DAILY_MUSIC
import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_HIGHTQUALITY
import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_MUSIC_LIST
import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_NEWEST_ALBUM
import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_NEW_MUSIC
import com.brins.baselib.config.RECOMMEND.Companion.RECOMMEND_TOP_SONG
import com.brins.networklib.model.album.AlbumResult
import com.brins.networklib.model.album.NewestAlbum
import com.brins.networklib.model.banner.BannerResult
import com.brins.networklib.model.music.TopMusic
import com.brins.networklib.model.music.TopMusicResult
import com.brins.networklib.model.musiclist.MusicList
import com.brins.networklib.model.musiclist.MusicListsResult
import com.brins.networklib.model.personal.PersonalizedMusic
import com.brins.networklib.model.personal.PersonalizedMusicList
import com.brins.networklib.model.personal.PersonalizedResult
import com.brins.networklib.model.recommend.RecommendResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author lipeilin
 * @date 2020/7/22
 */
interface PersonalizedService {


    @GET(RECOMMEND_MUSIC_LIST)
    fun getPersonalizedMusicList(@Query("limit") limit: Int = 10): Call<PersonalizedResult<PersonalizedMusicList>>

    /*
    * 加载轮播图
    * */
    @GET(BANNER)
    fun getBanner(@Query("type") type: Int = 1): Call<BannerResult>

    /**
     * 推荐新音乐
     *
     */
    @GET(RECOMMEND_NEW_MUSIC)
    fun getPersonalizedNewMusic(): Call<PersonalizedResult<PersonalizedMusic>>

    /**
     * 获取最新专辑
     *
     */
    @GET(RECOMMEND_NEWEST_ALBUM)
    fun getRecommendNewestAlbum(): Call<AlbumResult<NewestAlbum>>

    /**
     * TODO
     *
     * @param type 全部:0 华语:7 欧美:96 日本:8 韩国:16
     * @return
     */
    @GET(RECOMMEND_TOP_SONG)
    fun getRecommendTopSong(@Query("type") type: Int = 0): Call<TopMusicResult<TopMusic>>

    /**
     *
     * 获取推荐，网友精选
     * @param limit
     * @param order
     * @return
     */
    @GET(RECOMMEND_ALBUM)
    fun getTopRecommendw(
        @Query("limit") limit: Int = 10,
        @Query("order") order: String
    ): Call<RecommendResult<MusicList>>

    /**
     * 获取高品质歌单
     *
     * @param limit
     * @return
     */
    @GET(RECOMMEND_HIGHTQUALITY)
    fun getTopHightQuality(
        @Query("cat") cat: String,
        @Query("limit") limit: Int = 21
    ): Call<MusicListsResult>

    /**
     *
     *
     */
    @GET(RECOMMEND_DAILY_MUSIC)
    fun getDailyMusicRecommend() {

    }
}