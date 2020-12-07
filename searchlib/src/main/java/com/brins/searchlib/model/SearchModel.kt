package com.brins.searchlib.model

import com.brins.baselib.module.*
import com.brins.baselib.utils.GsonUtils
import com.brins.baselib.utils.SpUtils
import com.brins.baselib.utils.SpUtils.KEY_SEARCH_HISTORY
import com.brins.baselib.utils.SpUtils.SP_SEARCH_INFO
import com.brins.baselib.utils.UIUtils
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.follow.FollowOrUnFollowResult
import com.brins.networklib.model.search.HotSearchResult
import com.brins.networklib.model.search.SearchResult
import com.brins.searchlib.contract.SearchContract

/**
 * Created by lipeilin
 * on 2020/11/15
 */
class SearchModel : SearchContract.Model {

    override fun loadHistorySearch(): ArrayList<String>? {
        val str =
            SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext()).getString(KEY_SEARCH_HISTORY, "")
        val list = GsonUtils.listFromJson<String>(str)
        return list
    }

    override suspend fun loadHotSearch(): HotSearchResult =
        ApiHelper.getSearchService().loadHotSearch().await()

    override suspend fun loadSearchSuggest(input: String) =
        ApiHelper.getSearchService().loadSearchSuggest(input).await()

    override suspend fun loadMusicSearchResult(input: String, type: Int): SearchResult<BaseMusic> =
        ApiHelper.getSearchService().loadMusicSearchResult(input, type).await()

    override suspend fun followUser(id: String): FollowOrUnFollowResult =
        ApiHelper.getSearchService().followUser(id).await()


    override suspend fun loadAlbumSearchResult(
        input: String,
        type: Int
    ): SearchResult<BaseMusic.Album> =
        ApiHelper.getSearchService().loadAlbumSearchResult(input).await()

    override suspend fun loadArtistSearchResult(
        input: String,
        type: Int
    ): SearchResult<BaseMusic.Artist> =
        ApiHelper.getSearchService().loadArtistSearchResult(input).await()

    override suspend fun loadMusicListSearchResult(
        input: String,
        type: Int
    ): SearchResult<MusicList> =
        ApiHelper.getSearchService().loadMusicListSearchResult(input).await()

    override suspend fun loadUserSearchResult(input: String, type: Int): SearchResult<UserProfile> =
        ApiHelper.getSearchService().loadUserSearchResult(input).await()

    override suspend fun loadMusicVideoSearchResult(
        input: String,
        type: Int
    ): SearchResult<BaseMusicVideo> =
        ApiHelper.getSearchService().loadMusicVideoSearchResult(input).await()

    override suspend fun loadRadioSearchResult(input: String, type: Int): SearchResult<DjRadio> =
        ApiHelper.getSearchService().loadRadioSearchResult(input).await()

    override fun addHistorySearch(search: String) {
        val str =
            SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext()).getString(KEY_SEARCH_HISTORY, "")
        var list = GsonUtils.listFromJson<String>(str)
        if (list == null) {
            list = ArrayList()
        }
        list.add(0, search)
        SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext())
            .save(KEY_SEARCH_HISTORY, GsonUtils.listToJson(list))
    }

    override fun clearHistorySearch() {
        SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext())
            .save(KEY_SEARCH_HISTORY, "")
    }

    override fun onDestroy() {

    }
}