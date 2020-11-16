package com.brins.networklib.service

import com.brins.baselib.config.SEARCH
import com.brins.baselib.config.SEARCH.Companion.SEARCH_SUGGEST
import com.brins.networklib.model.search.HotSearchResult
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

}