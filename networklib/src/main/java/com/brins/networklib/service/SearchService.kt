package com.brins.networklib.service

import com.brins.baselib.config.SEARCH
import com.brins.baselib.config.SEARCH.Companion.SEARCH_CLOUD
import com.brins.baselib.config.SEARCH.Companion.SEARCH_SUGGEST
import com.brins.baselib.module.*
import com.brins.networklib.model.search.HotSearchResult
import com.brins.networklib.model.search.SearchResult
import com.brins.networklib.model.search.SearchSuggestResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2020/11/15
 */
interface SearchService {

    @GET(SEARCH.SEARCH_HOT)
    fun loadHotSearch(): Call<HotSearchResult>

    @GET(SEARCH_SUGGEST)
    fun loadSearchSuggest(
        @Query("keywords") keys: String,
        @Query("type") type: String = "mobile"
    ): Call<SearchSuggestResult>

    @GET(SEARCH_CLOUD)
    fun loadMusicSearchResult(
        @Query("keywords") keys: String, @Query("type") type: Int = 1
    ): Call<SearchResult<BaseMusic>>

    @GET(SEARCH_CLOUD)
    fun loadAlbumSearchResult(
        @Query("keywords") keys: String, @Query("type") type: Int = 10
    ): Call<SearchResult<BaseMusic.Album>>

    @GET(SEARCH_CLOUD)
    fun loadArtistSearchResult(
        @Query("keywords") keys: String, @Query("type") type: Int = 100
    ): Call<SearchResult<BaseMusic.Artist>>

    @GET(SEARCH_CLOUD)
    fun loadMusicListSearchResult(
        @Query("keywords") keys: String, @Query("type") type: Int = 1000
    ): Call<SearchResult<MusicList>>

    @GET(SEARCH_CLOUD)
    fun loadUserSearchResult(
        @Query("keywords") keys: String, @Query("type") type: Int = 1002
    ): Call<SearchResult<UserProfile>>

    @GET(SEARCH_CLOUD)
    fun loadMusicVideoSearchResult(
        @Query("keywords") keys: String, @Query("type") type: Int = 1004
    ): Call<SearchResult<BaseMusicVideo>>

    @GET(SEARCH_CLOUD)
    fun loadRadioSearchResult(
        @Query("keywords") keys: String, @Query("type") type: Int = 1009
    ): Call<SearchResult<DjRadio>>
}